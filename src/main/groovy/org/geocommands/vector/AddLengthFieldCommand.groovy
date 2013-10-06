package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add a length/perimeter field to a Layer and set the values.
 * @author Jared Erickson
 */
class AddLengthFieldCommand extends LayerInOutCommand<AddLengthFieldOptions> {

    @Override
    String getName() {
        "vector addlengthfield"
    }

    @Override
    String getDescription() {
        "Add an length/perimeter Field"
    }

    @Override
    AddLengthFieldOptions getOptions() {
        new AddLengthFieldOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AddLengthFieldOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.eachFeature {Feature f ->
                Map attributes = f.attributes
                attributes[options.lengthFieldName] = f.geom.length
                outLayer.add(outLayer.schema.feature(attributes, f.id))
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, AddLengthFieldOptions options) {
        layer.schema.addField(new Field(options.lengthFieldName, "double"), getOutputLayerName(layer, "lengthfield", options))
    }

    static class AddLengthFieldOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--length-fieldname",  usage="The name for the length Field", required = false)
        String lengthFieldName = "LENGTH"

    }
}
