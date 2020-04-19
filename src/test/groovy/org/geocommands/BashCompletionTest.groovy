package org.geocommands

import org.junit.Test
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.OptionHandlerFilter

import javax.swing.tree.DefaultMutableTreeNode

class BashCompletionTest {

    @Test
    void createBashCompletion() {

        File file = new File("src/shell/geoc_bash_comp")
        if (!file.exists()) {
            file.mkdir()
        }

        String NEW_LINE = System.getProperty("line.separator")

        // Get all of the Commands
        List<Command> commands = ServiceLoader.load(Command.class).iterator().sort {
            it.name
        }.collect { Command cmd -> cmd }

        // Build a Tree structure based on the words that make up the Command's name
        def tree = new DefaultMutableTreeNode("geoc")
        commands.each { Command cmd ->
            build(tree, cmd.name.split(" ") as List)
        }

        // Begin writing the bash completion script
        StringBuilder builder = new StringBuilder()
        builder.append("_geoc()").append(NEW_LINE)
        builder.append("{").append(NEW_LINE)
        builder.append("    local cur=\${COMP_WORDS[COMP_CWORD]}").append(NEW_LINE)
        builder.append("    local line=\${COMP_LINE}").append(NEW_LINE)
        builder.append("    local cmd=\${line##*geoc }").append(NEW_LINE)
        builder.append("    COMPREPLY=()").append(NEW_LINE)

        boolean first = true
        Enumeration en = tree.depthFirstEnumeration();
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

            // If it's a leaf, it's a Command, so display the command line arguments
            if (node.isLeaf()) {
                String name = node.path.join(" ")
                String commandArgs = getArgs(name.replace("geoc ", ""), commands)
                builder.append("    ${!first ? 'el' : ''}if [[ \"\$line\" == *\"${name} \"* ]]; then").append(NEW_LINE)
                builder.append("        COMPREPLY=(\$(compgen -W '${commandArgs}' -- \$cur))").append(NEW_LINE)
            }
            // Else it's a grouping
            else {

                // Collect all the Children's names
                Set children = [] as Set
                (0..<node.childCount).collect { int i ->
                    children.add(node.getChildAt(i).path.join(" "))
                }

                // Then just get the last names (geoc vector buffer == buffer)
                Set childNames = children.collect { String nm ->
                    nm.substring(nm.lastIndexOf(' '))
                }

                String name = node.path.join(" ")
                builder.append("    elif [[ \"\$line\" == *\"${name} \" ]]; then").append(NEW_LINE)
                builder.append("        COMPREPLY=(\$(compgen -W '${childNames.join(' ')}'))").append(NEW_LINE)
                builder.append("    elif [[ \"\$line\" == *\"${name}\"[[:space:]][[:alpha:]]* ]]; then").append(NEW_LINE)

                children.each { String child ->
                    String lastName = child.substring(child.lastIndexOf(' '))
                    builder.append("        if [[ \"${child.replace("geoc ", "")}\" == \"\$cmd\"* ]]; then").append(NEW_LINE)
                    builder.append("            COMPREPLY=(\"\${COMPREPLY[@]}\" \$(compgen -W '${lastName}'))").append(NEW_LINE)
                    builder.append("        fi").append(NEW_LINE)
                }
            }
            first = false
        }

        builder.append("    fi").append(NEW_LINE)
        builder.append("    return 0").append(NEW_LINE)
        builder.append("} && complete -f -d -F _geoc geoc").append(NEW_LINE)

        // Write the file
        file.write(builder.toString())
    }

    /**
     * Look for a Node named 'path' in the parent 'root'
     * @param root The parent or root DefaultMutableTreeNode
     * @param path The name of the child node to look for
     * @return A DefaultMutableTreeNode or null if it doesn't exist
     */
    static DefaultMutableTreeNode find(DefaultMutableTreeNode root, String path) {
        for (int i = 0; i < root.childCount; i++) {
            DefaultMutableTreeNode node = root.getChildAt(i)
            if (node.getUserObject().toString().equals(path)) {
                return node
            }
        }
        return null
    }

    /**
     * Build up a Tree based on the List of words that make up a command
     * @param root The root Node (geoc)
     * @param name The command name (geoc vector buffer)
     * @return
     */
    static void build(final DefaultMutableTreeNode root, List paths) {
        DefaultMutableTreeNode localRoot = root
        paths.eachWithIndex { String path, int i ->
            DefaultMutableTreeNode node = find(localRoot, path)
            if (node == null) {
                node = new DefaultMutableTreeNode(path)
                localRoot.add(node)
            }
            localRoot = node
        }
    }

    /**
     * Get the arguments of a Command
     * @param commandName The Command name (geoc vector buffer)
     * @param commands The List of Commands
     * @return A String of arguments
     */
    static String getArgs(String commandName, List commands) {
        Command command = commands.find { Command cmd ->
            if (cmd.name.equalsIgnoreCase(commandName)) {
                return cmd
            }
        }
        CmdLineParser cmdLineParser = new CmdLineParser(command.options)
        StringBuilder b = new StringBuilder()
        cmdLineParser.printExample(OptionHandlerFilter.ALL).replaceAll("\\(", "").replaceAll("\\)", "").split(" ").each { String str ->
            if (str.startsWith("-")) {
                b.append(str).append(" ")
            }
        }
        b.toString().trim()
    }

}
