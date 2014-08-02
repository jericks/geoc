package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Calculate the minimum rectangle of each feature in the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class MinimumRectanglesCommand extends LayerInOutCommand<MinimumRectanglesOptions> {

    @Override
    String getName() {
        "vector minrects"
    }

    @Override
    String getDescription() {
        "Calculate the minimum rectangle of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    MinimumRectanglesOptions getOptions() {
        new MinimumRectanglesOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, MinimumRectanglesOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "minrects", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, MinimumRectanglesOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        values[k] = v.minimumRectangle
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }

    static class MinimumRectanglesOptions extends LayerInOutOptions {
    }
}
