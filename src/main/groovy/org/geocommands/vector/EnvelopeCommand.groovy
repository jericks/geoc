package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Get the bounding envelope of all the features of the input Layer and save it to the output Layer
 * @author Jared Erickson
 */
class EnvelopeCommand extends LayerInOutCommand<EnvelopeOptions> {

    @Override
    String getName() {
        "vector envelope"
    }

    @Override
    String getDescription() {
        "Get the bounding envelope of all the features of the input Layer and save it to the output Layer"
    }

    @Override
    EnvelopeOptions getOptions() {
        new EnvelopeOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, EnvelopeOptions options, Reader reader, Writer writer) {
        outLayer.add([inLayer.bounds.geometry])
    }

    @Override
    protected Schema createOutputSchema(Layer layer, EnvelopeOptions options) {
        new Schema(getOutputLayerName(layer, "envelope", options), [new Field("the_geom", "Polygon", layer.schema.proj)])
    }

    static class EnvelopeOptions extends LayerInOutOptions {
    }
}
