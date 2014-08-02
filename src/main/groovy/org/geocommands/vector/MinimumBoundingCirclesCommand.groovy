package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Calculate the minimum bounding circles of each feature in the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class MinimumBoundingCirclesCommand extends LayerInOutCommand<MinimumBoundingCirclesOptions> {

    @Override
    String getName() {
        "vector mincircles"
    }

    @Override
    String getDescription() {
        "Calculate the minimum bounding circles of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    MinimumBoundingCirclesOptions getOptions() {
        new MinimumBoundingCirclesOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, MinimumBoundingCirclesOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "mincircles", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, MinimumBoundingCirclesOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        values[k] = v.minimumBoundingCircle
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }

    static class MinimumBoundingCirclesOptions extends LayerInOutOptions {
    }
}
