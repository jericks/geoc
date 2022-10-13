package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Graticule
import geoscript.layer.Layer
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create line graticules.
 * @author Jared Erickson
 */
class GraticuleLineCommand extends LayerOutCommand<GraticuleLineOptions> {


    @Override
    String getName() {
        "vector graticule line"
    }

    @Override
    String getDescription() {
        "Create line graticules"
    }

    @Override
    GraticuleLineOptions getOptions() {
        new GraticuleLineOptions()
    }

    @Override
    Layer createLayer(GraticuleLineOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        Workspace workspace = Util.getWorkspace(options.outputWorkspace)
        Graticule.createLines(
                bounds,
                options.lineDefinitions.collect {
                    if (!(it instanceof Map)) {
                        Map definition = [:]
                        List values = it.split(",")
                        definition.orientation = values[0]
                        definition.level = values[1] as int
                        definition.spacing = values.size() > 2 ? values[2] as int : null
                        definition
                    } else {
                        it
                    }
                },
                options.spacing,
                workspace: workspace,
                layer: Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, "graticule")
        )
    }

    static class GraticuleLineOptions extends LayerOutOptions {

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-s", aliases = "--spacing", usage = "The spacing (defaults to -1)", required = false)
        double spacing = -1

        @Option(name = "-l", aliases = "--line-definition", usage = "Each line definition has comma delimited orientation (vertical or horizontal), level, and spacing)", required = true)
        List<String> lineDefinitions = []

    }

}
