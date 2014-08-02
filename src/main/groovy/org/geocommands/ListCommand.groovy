package org.geocommands

import org.kohsuke.args4j.Option

/**
 * List all geocommands
 * @author Jared Erickson
 */
class ListCommand extends Command<ListOptions> {

    String getName() {
        "list"
    }

    String getDescription() {
        "List all geocommands"
    }

    ListOptions getOptions() {
        new ListOptions()
    }

    void execute(ListOptions options, Reader reader, Writer writer) {
        String NEW_LINE = System.getProperty("line.separator")
        StringBuilder builder = new StringBuilder()
        ServiceLoader.load(Command.class).iterator().sort { it.name }.eachWithIndex { cmd, i ->
            if (i > 0) {
                builder.append(NEW_LINE)
            }
            builder.append(cmd.name)
            if (options.showDescriptions) {
                builder.append(" = ").append(cmd.description)
            }
        }
        writer.write(builder.toString())
    }

    static class ListOptions extends Options {

        @Option(name = "-d", aliases = "--description", usage = "Include the description", required = false)
        boolean showDescriptions = false

    }
}
