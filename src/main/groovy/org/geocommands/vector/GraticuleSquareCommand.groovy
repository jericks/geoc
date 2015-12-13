package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Graticule
import geoscript.layer.Layer
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create square graticules.
 * @author Jared Erickson
 */
class GraticuleSquareCommand extends LayerOutCommand<GraticuleSquareOptions>{


    @Override
    String getName() {
        "vector graticule square"
    }

    @Override
    String getDescription() {
        "Create square graticules"
    }

    @Override
    GraticuleSquareOptions getOptions() {
        new GraticuleSquareOptions()
    }

    @Override
    Layer createLayer(GraticuleSquareOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        Workspace workspace = Util.getWorkspace(options.outputWorkspace)
        Graticule.createSquares(
            bounds,
            options.length,
            options.spacing,
            workspace: workspace,
            layer: Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "graticule")
        )
    }

    static class GraticuleSquareOptions extends LayerOutOptions {

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-l", aliases = "--length", usage = "The length", required = true)
        double length

        @Option(name = "-s", aliases = "--spacing", usage = "The spacing (defaults to -1)", required = false)
        double spacing = -1

    }

}
