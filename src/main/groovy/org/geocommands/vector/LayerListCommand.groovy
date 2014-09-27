package org.geocommands.vector

import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * List Layers in a DataStore
 * @author Jared Erickson
 */
class LayerListCommand extends Command<LayerListOptions> {

    @Override
    String getName() {
        "vector list layers"
    }

    @Override
    String getDescription() {
        "List Layers in a DataStore"
    }

    @Override
    LayerListOptions getOptions() {
        new LayerListOptions()
    }

    @Override
    void execute(LayerListOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Workspace.getWorkspace(options.inputWorkspace)
        String NEW_LINE = System.getProperty("line.separator")
        StringBuilder builder = new StringBuilder()
        workspace.names.eachWithIndex { String name, int i ->
            if (i > 0) {
                builder.append(NEW_LINE)
            }
            builder.append(name)
        }
        writer.write(builder.toString())
    }

    static class LayerListOptions extends Options {
        @Option(name = "-i", aliases = "--input-workspace", usage = "The input workspace", required = false)
        String inputWorkspace
    }
}
