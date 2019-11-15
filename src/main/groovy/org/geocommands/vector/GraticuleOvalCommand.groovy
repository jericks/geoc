package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Graticule
import geoscript.layer.Layer
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create oval graticules.
 * @author Jared Erickson
 */
class GraticuleOvalCommand extends LayerOutCommand<GraticuleOvalOptions>{


    @Override
    String getName() {
        "vector graticule oval"
    }

    @Override
    String getDescription() {
        "Create oval graticules"
    }

    @Override
    GraticuleOvalOptions getOptions() {
        new GraticuleOvalOptions()
    }

    @Override
    Layer createLayer(GraticuleOvalOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        Workspace workspace = Util.getWorkspace(options.outputWorkspace)
        Graticule.createOvals(
            bounds,
            options.length,
            workspace: workspace,
            layer: Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "graticule")
        )
    }

    static class GraticuleOvalOptions extends LayerOutOptions {

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-l", aliases = "--length", usage = "The length", required = true)
        double length

    }

}
