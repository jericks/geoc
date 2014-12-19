package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * A Command to list indexes in a database.
 * @author Jared Erickson
 */
class DatabaseListIndexCommand extends Command<DatabaseListIndexOptions> {

    @Override
    String getName() {
        "vector database index list"
    }

    @Override
    String getDescription() {
        "List database indices"
    }

    @Override
    DatabaseListIndexOptions getOptions() {
        new DatabaseListIndexOptions()
    }

    @Override
    void execute(DatabaseListIndexOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            List indexes = database.getIndexes(options.layerName)
            if (options.prettyPrint) {
                int maxNameLength = Integer.MIN_VALUE
                int maxAttributesLength = Integer.MIN_VALUE
                indexes.each { Map index ->
                    maxNameLength = Math.max(maxNameLength, index.name.toString().length())
                    maxAttributesLength = Math.max(maxNameLength, index.attributes.join(" ").toString().length())
                }

                String header = "| ${'Name'.padRight(maxNameLength)} | ${'Unique'.padRight(8)} | ${'Attributes'.padRight(maxAttributesLength)} |"
                String line = "-".padLeft(header.length(), "-")
                writer.write(line)
                writer.write(NEW_LINE)
                writer.write(header)
                writer.write(NEW_LINE)
                writer.write(line)
                writer.write(NEW_LINE)
                indexes.each { Map index ->
                    writer.write("| ${index.name.padRight(maxNameLength)} | ${index.unique.toString().padRight(8)} | ${index.attributes.join(' ').padRight(maxAttributesLength)} |")
                    writer.write(NEW_LINE)
                }
                writer.write(line)
            } else {
                writer.write("name, unique, attributes")
                writer.write(NEW_LINE)
                indexes.each { Map index ->
                    writer.write("${index.name}, ${index.unique}, ${index.attributes.join(' ')}")
                    writer.write(NEW_LINE)
                }
            }
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseListIndexOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-l", aliases = "--layer-name", usage = "The input workspace", required = true)
        String layerName

        @Option(name = "-p", aliases = "--pretty-print", usage = "Whether to pretty print the output", required = false)
        boolean prettyPrint = false

    }
}
