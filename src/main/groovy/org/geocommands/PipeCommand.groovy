package org.geocommands

import org.kohsuke.args4j.Option
import org.geocommands.PipeCommand.PipeOptions

class PipeCommand extends Command<PipeOptions> {

    @Override
    String getName() {
        "pipe"
    }

    @Override
    String getDescription() {
        "Combine multiple commands together with a pipe."
    }

    @Override
    PipeOptions getOptions() {
        new PipeOptions()
    }

    @Override
    void execute(PipeOptions options, Reader reader, Writer writer) throws Exception {

        String[] commands = options.getCommands().split("\\|")

        Reader subReader = reader
        StringWriter subWriter = new StringWriter()
        StringWriter errorWriter = new StringWriter()
        String output = ""

        for(String command : commands) {
            String[] subArgs = translateCommandline(command.trim())
            Commands.execute(subArgs, subReader, subWriter, errorWriter)
            String errorMessage = errorWriter.toString().trim()
            if (!errorMessage.isEmpty()) {
                throw new Exception(errorMessage)
            }
            output = subWriter.toString()
            subReader = new StringReader(output)
            subWriter = new StringWriter()
            errorWriter = new StringWriter()
        }
        writer.write(output)
        writer.write("\n")
    }

    /**
     * From Ant
     */
    private String[] translateCommandline(String toProcess) {
        if (toProcess == null || toProcess.isEmpty()) {
            new String[0]
        }

        final int normal = 0
        final int inQuote = 1
        final int inDoubleQuote = 2
        int state = normal
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true)
        final ArrayList<String> result = new ArrayList<>()
        final StringBuilder current = new StringBuilder()
        boolean lastTokenHasBeenQuoted = false

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken()
            switch (state) {
                case inQuote:
                    if ("'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true
                        state = normal
                    } else {
                        current.append(nextTok)
                    }
                    break
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true
                        state = normal
                    } else {
                        current.append(nextTok)
                    }
                    break
                default:
                    if ("'".equals(nextTok)) {
                        state = inQuote
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() > 0) {
                            result.add(current.toString())
                            current.setLength(0)
                        }
                    } else {
                        current.append(nextTok)
                    }
                    lastTokenHasBeenQuoted = false
                    break
            }
        }
        if (lastTokenHasBeenQuoted || current.length() > 0) {
            result.add(current.toString())
        }
        if (state == inQuote || state == inDoubleQuote) {
            throw new IllegalArgumentException("unbalanced quotes in " + toProcess)
        }
        result.toArray(new String[result.size()])
    }

    static class PipeOptions extends Options {

        @Option(name = "-c", aliases = "--commands", usage="Commands separate by pipe", required = true)
        String commands

    }

}
