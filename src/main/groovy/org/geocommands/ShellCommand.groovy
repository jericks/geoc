package org.geocommands

import org.jline.builtins.Completers.FileNameCompleter
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import org.jline.reader.ParsedLine
import org.jline.reader.impl.DefaultParser
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.TerminalBuilder
import org.jline.terminal.Terminal
import org.jline.reader.MaskingCallback
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.jline.utils.InfoCmp
import org.kohsuke.args4j.CmdLineParser

import java.awt.Desktop

class ShellCommand extends Command<ShellOptions> {

    private final static String NEW_LINE = System.getProperty("line.separator")

    String getName() {
        "shell"
    }

    String getDescription() {
        "Start an interactive shell"
    }

    ShellOptions getOptions() {
        new ShellOptions()
    }

    private static final String banner = """                     
                              _          _ _ 
                             | |        | | |
   __ _  ___  ___   ___   ___| |__   ___| | |
  / _` |/ _ \\/ _ \\ / __| / __| '_ \\ / _ \\ | |
 | (_| |  __/ (_) | (__  \\__ \\ | | |  __/ | |
  \\__, |\\___|\\___/ \\___| |___/_| |_|\\___|_|_|
   __/ |                                     
  |___/                                                        
"""

    void execute(ShellOptions shellOptions, Reader reader, Writer writer) {

        File historyFile = new File("geoc.log")

        Terminal terminal = TerminalBuilder.builder()
                .streams(System.in, System.out)
                .system(true)
                .build()

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new GeocCompleter())
                .parser(new DefaultParser())
                .variable(LineReader.HISTORY_FILE, historyFile)
                .history(new DefaultHistory())
                .build()

        terminal.writer().println(banner)

        String prompt = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .style(AttributedStyle.BOLD)
                .append("geoc> ")
                .style(AttributedStyle.DEFAULT)
                .toAnsi()
        String rightPrompt = null

        String rawLine
        while (true) {
            try {
                rawLine = lineReader.readLine(prompt, rightPrompt, (MaskingCallback) null, null)
                ParsedLine parsedLine = lineReader.getParser().parse(rawLine, 0)
                String line = parsedLine.line()
                if (line.equals("clear")) {
                    terminal.puts(InfoCmp.Capability.clear_screen);
                } else {
                    run(parsedLine.words() as String[], terminal.writer(), new StringReader(""))
                }
            } catch (UserInterruptException e) {
                // Ignore
            } catch (EndOfFileException e) {
                return;
            }
        }
    }

    private void run(String[] args, Writer writer, Reader reader) {

        // Usage string
        final String usage = "<command> <args>"

        // Get the command name
        String name = args.takeWhile { String arg ->
            !arg.startsWith("-")
        }.join(" ")

        if (name.equalsIgnoreCase("exit")) {
            System.exit(0)
        }

        // Lookup the Command by name
        Command command = ServiceLoader.load(Command.class).find { cmd ->
            if (cmd.name.equalsIgnoreCase(name)) {
                return cmd
            }
        }

        // If we couldn't find the Command error out
        if (command == null) {
            writer.write(error("Unknown command: '" + name + "'!" + NEW_LINE))
            return
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
                writer.write(command.getName() + ": " + command.getDescription() + NEW_LINE)
                printUsage(cmdLineParser, writer)
            }
            else if (options.isWebHelp()) {
                URI uri = new URI("http://jericks.github.io/geoc/commands/${options.names.join('-')}.html")
                Desktop.desktop.browse(uri)
            }
            else {
                // If there are no errors, execute the command
                command.execute(options, reader, writer)
                writer.write(NEW_LINE)
                writer.flush()
            }
        } catch (Exception e) {
            // Print help for the command (if required options are not present)
            if (options.isHelp()) {
                writer.write(command.getName() + ": " + command.getDescription() + NEW_LINE)
                printUsage(cmdLineParser, writer)
            }
            else if (options.isWebHelp()) {
                URI uri = new URI("http://jericks.github.io/geoc/commands/${options.names.join('-')}.html")
                Desktop.desktop.browse(uri)
            }
            else {
                // Oops, display the error messages to the user
                writer.write(error(e.getMessage() + NEW_LINE))
                writer.write(usage + NEW_LINE)
                printUsage(cmdLineParser, writer)
            }
        }
    }

    private String error(String message) {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .style(AttributedStyle.DEFAULT)
                .append(message)
                .style(AttributedStyle.DEFAULT)
                .toAnsi()
    }

    private void printUsage(CmdLineParser cmdLineParser, Writer writer) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        cmdLineParser.printUsage(outputStream)
        writer.write(outputStream.toString() + NEW_LINE)
    }

    private static class GeocCompleter implements Completer {

        private final org.geocommands.Completer completer

        private final FileNameCompleter fileNameCompleter

        GeocCompleter() {
            this.completer = new org.geocommands.Completer()
            fileNameCompleter = new FileNameCompleter()
        }

        @Override
        void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
            completer.complete(parsedLine.line()).each { String candidate ->
                list.add(new Candidate(candidate))
            }
            if (parsedLine.line().contains("-")) {
                fileNameCompleter.complete(lineReader, parsedLine, list)
            }
        }

    }

    static class ShellOptions extends Options {

    }

}
