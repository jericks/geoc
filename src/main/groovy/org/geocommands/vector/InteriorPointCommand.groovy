package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Get the interior point of each feature in the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class InteriorPointCommand extends LayerInOutCommand<InteriorPointOptions> {

    @Override
    String getName() {
        "vector interiorpoint"
    }

    @Override
    String getDescription() {
        "Get the interior point of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    InteriorPointOptions getOptions() {
        new InteriorPointOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, InteriorPointOptions options, Reader reader, Writer writer) {
        inLayer.eachFeature {Feature f ->
            Map values = [:]
            f.attributes.each{k,v ->
                if (v instanceof geoscript.geom.Geometry) {
                    values[k] = v.interiorPoint
                } else {
                    values[k] = v
                }
            }
            outLayer.add(values)
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, InteriorPointOptions options) {
        layer.schema.changeGeometryType("Point", getOutputLayerName(layer, "interiorpoint", options))
    }

    static class InteriorPointOptions extends LayerInOutOptions{
    }
}
