package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Graticule
import geoscript.layer.Layer
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create hexagon graticules.
 * @author Jared Erickson
 */
class GraticuleHexagonCommand extends LayerOutCommand<GraticuleHexagonOptions>{


    @Override
    String getName() {
        "vector graticule hexagon"
    }

    @Override
    String getDescription() {
        "Create hexagon graticules"
    }

    @Override
    GraticuleHexagonOptions getOptions() {
        new GraticuleHexagonOptions()
    }

    @Override
    Layer createLayer(GraticuleHexagonOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        Workspace workspace = Util.getWorkspace(options.outputWorkspace)
        Graticule.createHexagons(
                bounds,
                options.length,
                options.spacing,
                options.orientation,
                workspace: workspace,
                layer: Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "graticule")
        )
    }

    static class GraticuleHexagonOptions extends LayerOutOptions {

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-l", aliases = "--length", usage = "The length", required = true)
        double length

        @Option(name = "-s", aliases = "--spacing", usage = "The spacing (defaults to -1)", required = false)
        double spacing = -1

        @Option(name = "-t", aliases = "--orientation", usage = "The orientation (flat or angled).", required = false)
        String orientation = "flat" // or angled

    }

}
