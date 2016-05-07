package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * A Command to create an index of a database table
 * @author Jared Erickson
 */
class DatabaseCreateIndexCommand extends Command<DatabaseCreateIndexOptions> {

    @Override
    String getName() {
        "vector database index create"
    }

    @Override
    String getDescription() {
        "Create a database index"
    }

    @Override
    DatabaseCreateIndexOptions getOptions() {
        new DatabaseCreateIndexOptions()
    }

    @Override
    void execute(DatabaseCreateIndexOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            database.createIndex(options.layerName, options.indexName, options.fieldNames, options.unique)
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseCreateIndexOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-l", aliases = "--layer-name", usage = "The input workspace", required = true)
        String layerName

        @Option(name = "-i", aliases = "--index-name", usage = "The input workspace", required = true)
        String indexName

        @Option(name = "-f", aliases = "--field", usage = "The input workspace", required = true)
        List<String> fieldNames

        @Option(name = "-u", aliases = "--unique", usage = "The input workspace", required = false)
        boolean unique
    }

}
