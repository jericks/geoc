package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.feature.Feature
import geoscript.feature.Schema

/**
 *
 */
class CentroidCommand extends LayerInOutCommand<CentroidOptions> {

    @Override
    String getName() {
        "vector centroid"
    }

    @Override
    String getDescription() {
        "Get the centroid of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    CentroidOptions getOptions() {
        new CentroidOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, CentroidOptions options, Reader reader, Writer writer) {
        inLayer.eachFeature {Feature f ->
            Map values = [:]
            f.attributes.each{k,v ->
                if (v instanceof geoscript.geom.Geometry) {
                    values[k] = v.centroid
                } else {
                    values[k] = v
                }
            }
            outLayer.add(values)
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, CentroidOptions options) {
        layer.schema.changeGeometryType("Point", getOutputLayerName(layer, "centroid", options))
    }

    static class CentroidOptions extends LayerInOutOptions {
    }
}
