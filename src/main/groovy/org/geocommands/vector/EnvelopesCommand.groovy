package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * Calculate the envelope of each feature in the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class EnvelopesCommand extends LayerInOutCommand<EnvelopesOptions> {

    @Override
    String getName() {
        "vector envelopes"
    }

    @Override
    String getDescription() {
        "Calculate the envelope of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    EnvelopesOptions getOptions() {
        new EnvelopesOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, EnvelopesOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "envelopes", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, EnvelopesOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        values[k] = v.bounds.geometry
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }

    static class EnvelopesOptions extends LayerInOutOptions {
    }
}
