package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add an ID field to a Layer and set the values.
 * @author Jared Erickson
 */
class AddIdFieldCommand extends LayerInOutCommand<AddIdFieldOptions> {

    @Override
    String getName() {
        "vector addidfield"
    }

    @Override
    String getDescription() {
        "Add an ID Field"
    }

    @Override
    AddIdFieldOptions getOptions() {
        new AddIdFieldOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AddIdFieldOptions options, Reader reader, Writer writer) throws Exception {
        int c = options.start
        inLayer.eachFeature {Feature f ->
            Map attributes = f.attributes
            attributes[options.idFieldName] = c
            outLayer.add(attributes)
            c++
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, AddIdFieldOptions options) {
        layer.schema.addField(new Field(options.idFieldName, "int"), getOutputLayerName(layer, "idfield", options))
    }

    static class AddIdFieldOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--id-fieldname",  usage="The name for the ID Field", required = false)
        String idFieldName = "ID"

        @Option(name="-s", aliases="--start",  usage="The number of start at", required = false)
        int start = 1

    }
}
