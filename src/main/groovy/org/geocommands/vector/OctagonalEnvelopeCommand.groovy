package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

/**
 * Calculate the octagonal envelope of the input Layer and save it to the output Layer
 * @author Jared Erickson
 */
class OctagonalEnvelopeCommand extends LayerInOutCommand<OctagonalEnvelopeOptions> {

    @Override
    String getName() {
        "vector octagonalenvelope"
    }

    @Override
    String getDescription() {
        "Calculate the octagonal envelope of the input Layer and save it to the output Layer"
    }

    @Override
    OctagonalEnvelopeOptions getOptions() {
        new OctagonalEnvelopeOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, OctagonalEnvelopeOptions options) {
        new Schema(getOutputLayerName(layer, "octagonalenvelope", options), [new Field(layer.schema.geom.name, "Polygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, OctagonalEnvelopeOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature {f ->
            f.geom
        })
        outLayer.add([geoms.octagonalEnvelope])
    }

    static class OctagonalEnvelopeOptions extends LayerInOutOptions {
    }
}
