package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Remove a Layer from a Database.
 * @author Jared Erickson
 */
class DatabaseRemoveCommand extends Command<DatabaseRemoveOptions> {

    @Override
    String getName() {
        "vector database remove"
    }

    @Override
    String getDescription() {
        "Remove a Layer from a Database"
    }

    @Override
    DatabaseRemoveOptions getOptions() {
        new DatabaseRemoveOptions()
    }

    @Override
    void execute(DatabaseRemoveOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            database.remove(options.layerName)
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseRemoveOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-l", aliases = "--layer-name", usage = "The input workspace", required = true)
        String layerName

    }
}
