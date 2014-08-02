package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Calculate the octagonal envelope of each feature in the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class OctagonalEnvelopesCommand extends LayerInOutCommand<OctagonalEnvelopesOptions> {

    @Override
    String getName() {
        "vector octagonalenvelopes"
    }

    @Override
    String getDescription() {
        "Calculate the octagonal envelope of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    OctagonalEnvelopesOptions getOptions() {
        new OctagonalEnvelopesOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, OctagonalEnvelopesOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "octagonalenvelopes", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, OctagonalEnvelopesOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        values[k] = v.octagonalEnvelope
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }

    static class OctagonalEnvelopesOptions extends LayerInOutOptions {
    }
}
