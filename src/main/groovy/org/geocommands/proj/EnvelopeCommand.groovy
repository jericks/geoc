package org.geocommands.proj

import geoscript.feature.Field
import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.proj.Projection
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * A Command to get a Projection's Bounds
 * @author Jared Erickson
 */
class EnvelopeCommand extends Command<EnvelopeOptions> {

    @Override
    String getName() {
        "proj envelope"
    }

    /**
     * Get the short description
     * @return A short description
     */
    @Override
    String getDescription() {
        "Get a Projection's envelope"
    }

    /**
     * Get the Options
     * @return The Options
     */
    @Override
    EnvelopeOptions getOptions() {
        new EnvelopeOptions()
    }

    /**
     * Execute the command
     * @param options The Options
     * @param reader The Reader
     * @param writer The Write
     * @throws Exception if an error occurs
     */
    @Override
    void execute(EnvelopeOptions options, Reader reader, Writer writer) throws Exception {
        // Get Projection
        Projection proj = new Projection(options.epsg)
        // Create Layer
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = Workspace.getWorkspace(options.outputWorkspace)
        }
        Layer layer = workspace.create(org.geocommands.vector.Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "proj"), [
            new Field("the_geom", "POLYGON", options.geoBounds ? "EPSG:4326" : proj),
            new Field("epsg", "int")
        ])
        // Add Bounds
        Bounds bounds = options.geoBounds ? proj.geoBounds : proj.bounds
        layer.add([
                the_geom: bounds.geometry,
                epsg: options.epsg
        ])
        // Write Layer
        try {
            if (layer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(layer))
            }
        }
        finally {
            layer.workspace.close()
        }
    }

    static class EnvelopeOptions extends Options {

        @Option(name = "-e", aliases = "--epsg", usage = "The EPSG Projection code", required = true)
        String epsg

        @Option(name = "-g", aliases = "--geo-bounds", usage = "The flag for whether to use geo bounds or not", required = false)
        boolean geoBounds

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace

        @Option(name = "-r", aliases = "--output-layer", usage = "The output layer", required = false)
        String outputLayer

    }
}
