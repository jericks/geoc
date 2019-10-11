package org.geocommands

import groovy.transform.Canonical
import org.kohsuke.args4j.Option

import java.lang.reflect.Field

class Completer {

    private final Tree tree

    private final List<String> baseNames

    Completer() {
        this.tree = new Tree()
        Commands.list().each { Command cmd ->
            this.tree.findOrCreateItem(cmd.name.split(" ") as List)
        }
        this.baseNames = getBaseCommands()
    }

    private List<String> getBaseCommands() {
        Set<String> baseNames = []
        Commands.list().each { Command cmd ->
            baseNames.add(cmd.name.split(" ")[0])
        }
        baseNames as List
    }

    List<String> complete(String line) {
        // If the line is empty, just return the base root command names
        if (line.isEmpty()) {
           this.baseNames
        }
        // If the the line matches a command or the user has started adding options
        else if (Commands.find(line.trim()) || line.contains("-")) {
            List<String> options = []
            int end = line.contains("-") ? line.indexOf("-") : line.length()
            String name = line.substring(0, end).trim()
            getOptions(Commands.find(name)).each { CommandLineOptions cliOptions ->
                options.add(cliOptions.name)
                cliOptions.aliases.each { String alias ->
                    options.add(alias)
                }
            }
            return options
        }
        // Otherwise, try to look the line up in a tree or command names
        else {
            List<String> names = line.split(" ")
            Item item = this.tree.findItem(names)
            if (item) {
                return item.children.collect { it.name }
            } else {
                if (names.size() == 1) {
                    this.baseNames
                } else {
                    item = this.tree.findItem(names.subList(0, names.size() - 1))
                    if (item) {
                        return item.children.collect { it.name }
                    } else {
                        return []
                    }
                }
            }
        }
    }

    private static class Tree {

        List<Item> items = []

        Tree addItem(Item item) {
            this.items.add(item)
            this
        }

        private Item findOrCreate(List<String> names, boolean create) {
            Iterator<String> nameIterator = names.iterator()
            List<Item> itemsToSearch = this.items
            Item parentItem
            while(nameIterator.hasNext()) {
                String name = nameIterator.next()
                Item item = findItemInList(name, itemsToSearch)
                if (item) {
                    if(nameIterator.hasNext()) {
                        parentItem = item
                        itemsToSearch = item.children
                    } else {
                        return item
                    }
                } else {
                    if (create) {
                        Item newItem = new Item(name)
                        if (parentItem == null) {
                            addItem(newItem)
                        } else {
                            parentItem.addItem(newItem)
                        }
                        if(nameIterator.hasNext()) {
                            parentItem = newItem
                            itemsToSearch = newItem.children
                        } else {
                            return newItem
                        }
                    } else {
                        return null
                    }
                }
            }
        }

        Item findItem(List<String> names) {
            findOrCreate(names, false)
        }

        Item findOrCreateItem(List<String> names) {
            findOrCreate(names, true)
        }

        private Item findItemInList(String name, List<Item> items) {
            items.find { Item item -> item.name.equalsIgnoreCase(name) }
        }

    }

    private static class Item {

        String name
        Item parent
        List<Item> children = []

        Item(String name) {
            this(null, name)
        }

        Item(Item parent, String name) {
            this.parent = parent
            this.name = name
        }

        Item addItem(Item item) {
            item.parent = this
            this.children.add(item)
            this
        }

        String getFullName() {
            List parents = []
            Item p = this.parent
            while(p!=null) {
                parents.add(p.name)
                p = p.parent
            }
            "${parents.reverse().join(' ')} ${name}".trim()
        }

        String toString() {
            getFullName() + (children.isEmpty() ? "" : "(${children.join(', ')})")
        }


    }

    @Canonical
    private static class CommandLineOptions {
        final String name
        final String description
        final boolean required
        final List<String> aliases
    }

    private List<CommandLineOptions> getOptions(Command cmd) {
        List<CommandLineOptions> options = []
        if (cmd != null) {
            Class clazz = cmd.getOptions().class
            while (clazz != null) {
                clazz.getDeclaredFields().each { Field fld ->
                    fld.getAnnotationsByType(Option).each { Option opt ->
                        options.add(new CommandLineOptions(
                                opt.name(), opt.usage(), opt.required(), opt.aliases() as List
                        ))
                    }
                }
                clazz = clazz.superclass
            }
        }
        options
    }

}
