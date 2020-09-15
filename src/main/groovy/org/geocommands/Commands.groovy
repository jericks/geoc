package org.geocommands

import org.kohsuke.args4j.CmdLineParser
import java.awt.Desktop

class Commands {

    static List<Command> list() {
        ServiceLoader.load(Command).iterator().collect()
    }
    
    static Command find(String name) {
        list().find{ Command command ->
            command.name.equalsIgnoreCase(name)
        }
    }

    static void execute(String name, Map<String,Object> properties, Reader reader, Writer writer) {
        Command command = find(name)
        Options options = command.options
        properties.each { String key, Object value ->
            if (options.hasProperty(key)) {
                options.setProperty(key, value)
            }
        }
        command.execute(options, reader, writer)
    }

    static String execute(String name, Map<String,Object> properties) {
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        execute(name, properties, reader, writer)
        writer.toString()
    }

    static void execute(String[] args, Reader inputReader, Writer outputWriter, Writer errorWriter) {

        // The usage
        final String usage = "Usage: geoc <command> <args>"

        // Check for an argument (the command name is required)
        if (args.length == 0) {
            println(errorWriter,"Please enter a geocommand!")
            println(errorWriter,usage)
            flushSilently(errorWriter)
            throw new CommandException()
        }

        // Get the command name
        String name = args.takeWhile { String arg ->
            !arg.startsWith("-")
        }.join(" ")

        // Lookup the Command by name
        Command command = ServiceLoader.load(Command.class).find { cmd ->
            if (cmd.name.equalsIgnoreCase(name)) {
                return cmd
            }
        }

        // If we couldn't find the Command error out
        if (command == null) {
            println(errorWriter, "Unknown geocommand: '" + name + "'!")
            println(errorWriter, usage)
            flushSilently(errorWriter)
            throw new CommandException()
        }

        // Get the empty command line POJO
        Options options = command.getOptions()

        // Fill the command line POJO using args4j's CmdLineParser
        CmdLineParser cmdLineParser = new CmdLineParser(options)
        try {
            // Parse the arguments
            cmdLineParser.parseArgument(args)
            // Print help for the command
            if (options.isHelp()) {
                println(outputWriter,"geoc " + command.getName() + ": " + command.getDescription())
                cmdLineParser.printUsage(outputWriter, null)
            }
            else if (options.isWebHelp()) {
                URI uri = new URI("http://jericks.github.io/geoc/commands/${options.names.join('-')}.html")
                Desktop.desktop.browse(uri)
            }
            else {
                // If there are no errors, execute the command
                Writer writer = new StringWriter()
                command.execute(options, inputReader, writer)
                writer.flush()
                String output = writer.toString()
                if (!output.endsWith("\n")) {
                    println(outputWriter, output)
                } else {
                    outputWriter.write(output)
                }

                flushSilently(outputWriter)
            }
        } catch (Exception e) {
            // Print help for the command (if required options are not present)
            if (options.isHelp()) {
                println(outputWriter,"geoc " + command.getName() + ": " + command.getDescription())
                cmdLineParser.printUsage(outputWriter, null)
            }
            else if (options.isWebHelp()) {
                URI uri = new URI("http://jericks.github.io/geoc/commands/${options.names.join('-')}.html")
                Desktop.desktop.browse(uri)
            }
            else {
                // Oops, display the error messages to the user
                println(errorWriter,e.getMessage())
                println(errorWriter,usage)
                cmdLineParser.printUsage(errorWriter, null)
            }
        }
    }

    static class CommandException extends Exception {
    }

    private static void println(Writer writer, String message) {
        try {
            writer.write(message)
            writer.write("\n")
        } catch (IOException e) {
        }
    }

    private static void flushSilently(Writer writer) {
        try {
            writer.flush()
        }
        catch (IOException e) {
        }
    }

}
