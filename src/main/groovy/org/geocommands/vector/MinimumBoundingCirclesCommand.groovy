package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.feature.Feature
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

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
        inLayer.eachFeature {Feature f ->
            Map values = [:]
            f.attributes.each{k,v ->
                if (v instanceof geoscript.geom.Geometry) {
                    values[k] = v.minimumBoundingCircle
                } else {
                    values[k] = v
                }
            }
            outLayer.add(values)
        }
    }

    static class MinimumBoundingCirclesOptions extends LayerInOutOptions{
    }
}
