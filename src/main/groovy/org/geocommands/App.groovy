package org.geocommands

import org.kohsuke.args4j.CmdLineParser
import java.util.logging.*

class App {

    static void main(String[] args) {

        // Set XY coordinate ordering
        System.setProperty("org.geotools.referencing.forceXY", "true")

        // Turn off logging
        LogManager.getLogManager().reset();
        Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        globalLogger.setLevel(Level.OFF);

        // The usage
        final String usage = "Usage: geoc <command> <args>"

        // Check for an argument (the command name is required)
        if (args.length == 0) {
            System.err.println("Please enter a geocommand!")
            System.err.println(usage)
            System.exit(-1)
        }

        // Get the command name
        String name = args.takeWhile { String arg ->
            !arg.startsWith("-");
        }.join(" ")

        // Lookup the Command by name
        Command command = ServiceLoader.load(Command.class).find {cmd ->
            if (cmd.name.equalsIgnoreCase(name)) {
                return cmd
            }
        }

        // If we couldn't find the Command error out
        if (command == null) {
            System.err.println("Unknown geocommand: '" + name + "'!")
            System.err.println(usage)
            System.exit(-1)
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
                System.out.println("geoc " + command.getName() + ": " + command.getDescription())
                cmdLineParser.printUsage(System.out)
            } else {
                // If there are no errors, execute the command
                Reader reader = new InputStreamReader(System.in)
                Writer writer = new StringWriter()
                command.execute(options, reader, writer)
                writer.flush()
                String output = ((StringWriter)writer).getBuffer().toString()
                if (!output.isEmpty()) {
                    System.out.println(output)
                }
            }
        } catch (Exception e) {
            // Print help for the command (if required options are not present)
            if (options.isHelp()) {
                System.out.println("geoc " + command.getName() + ": " + command.getDescription())
                cmdLineParser.printUsage(System.out)
            } else {
                // Oops, display the error messages to the user
                e.printStackTrace()
                System.err.println(e.getMessage())
                System.err.println(usage)
                cmdLineParser.printUsage(System.err)
            }
        }
    }
}
