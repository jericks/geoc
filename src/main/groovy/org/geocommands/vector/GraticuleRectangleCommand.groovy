package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Graticule
import geoscript.layer.Layer
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create rectangle graticules.
 * @author Jared Erickson
 */

class GraticuleRectangleCommand extends LayerOutCommand<GraticuleRectangleOptions>{


    @Override
    String getName() {
        "vector graticule rectangle"
    }

    @Override
    String getDescription() {
        "Create rectangle graticules"
    }

    @Override
    GraticuleRectangleOptions getOptions() {
        new GraticuleRectangleOptions()
    }

    @Override
    Layer createLayer(GraticuleRectangleOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        Workspace workspace = Util.getWorkspace(options.outputWorkspace)
        Graticule.createRectangles(
                bounds,
                options.width,
                options.height,
                options.spacing,
                workspace: workspace,
                layer: Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "graticule")
        )
    }

    static class GraticuleRectangleOptions extends LayerOutOptions {

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-w", aliases = "--width", usage = "The width", required = true)
        double width

        @Option(name = "-h", aliases = "--height", usage = "The height", required = true)
        double height

        @Option(name = "-s", aliases = "--spacing", usage = "The spacing (defaults to -1)", required = false)
        double spacing = -1

    }

}
