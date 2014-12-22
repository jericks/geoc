package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * A command to delete a database index.
 * @author Jared Erickson
 */
class DatabaseIndexDeleteCommand extends Command<DatabaseIndexDeleteOptions> {

    @Override
    String getName() {
        "vector database index delete"
    }

    @Override
    String getDescription() {
        "Delete a database index"
    }

    @Override
    DatabaseIndexDeleteOptions getOptions() {
        new DatabaseIndexDeleteOptions()
    }

    @Override
    void execute(DatabaseIndexDeleteOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            database.deleteIndex(options.layerName, options.indexName)
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseIndexDeleteOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-l", aliases = "--layer-name", usage = "The input workspace", required = true)
        String layerName

        @Option(name = "-i", aliases = "--index-name", usage = "The input workspace", required = true)
        String indexName

    }
}
