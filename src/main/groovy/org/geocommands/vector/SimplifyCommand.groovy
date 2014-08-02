package org.geocommands.vector

import geoscript.geom.Geometry
import org.kohsuke.args4j.Option

/**
 * Simplify the features of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class SimplifyCommand extends TransformGeometryCommand<SimplifyOptions> {

    @Override
    String getName() {
        "vector simplify"
    }

    @Override
    String getDescription() {
        "Simplify the features of the input Layer and save them to the output Layer"
    }

    @Override
    SimplifyOptions getOptions() {
        new SimplifyOptions()
    }

    @Override
    Geometry transformGeometry(Geometry geometry, SimplifyOptions options) {
        if (options.algorithm.equalsIgnoreCase("douglaspeucker") || options.algorithm.equalsIgnoreCase("dp")) {
            geometry.simplify(options.distanceTolerance)
        } else /*if (options.algorithm.equalsIgnoreCase("TopologyPreserving") || options.algorithm.equalsIgnoreCase("tp")*/ {
            geometry.simplifyPreservingTopology(options.distanceTolerance)
        }
    }

    static class SimplifyOptions extends LayerInOutOptions {
        @Option(name = "-a", aliases = "--algorithm", usage = "The simplify algorithm (DouglasPeucker - dp or TopologyPreserving - tp)", required = false)
        String algorithm = "TopologyPreserving"

        @Option(name = "-d", aliases = "--distance", usage = "The distance tolerance", required = true)
        double distanceTolerance
    }
}


