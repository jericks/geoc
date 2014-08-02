package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Count the features, geometries, or points in a Layer
 * @author Jared Erickson
 */
class CountCommand extends LayerCommand<CountOptions> {

    String getName() {
        "vector count"
    }

    String getDescription() {
        "Count the features, geometries, or points in a Layer"
    }

    CountOptions getOptions() {
        new CountOptions()
    }

    @Override
    protected void processLayer(Layer layer, CountOptions options, Reader reader, Writer writer) {
        if (options.type.equalsIgnoreCase("features")) {
            writer.write("${layer.count}")
        } else if (options.type.equalsIgnoreCase("geometries")) {
            int numGeometries = 0
            layer.eachFeature { Feature f ->
                numGeometries += f.geom.numGeometries
            }
            writer.write("${numGeometries}")
        } else if (options.type.equalsIgnoreCase("points")) {
            int numPoints = 0
            layer.eachFeature { Feature f ->
                numPoints += f.geom.numPoints
            }
            writer.write("${numPoints}")
        } else {
            throw IllegalArgumentException("Unknown type '${options.type}!")
        }
    }

    static class CountOptions extends LayerOptions {

        @Option(name = "-t", aliases = "--type", usage = "Count features, geometries, or points", required = false)
        String type = "features"

    }
}
