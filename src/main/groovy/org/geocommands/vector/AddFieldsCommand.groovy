package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add one or more Fields to the input Layer to create the output Layer
 * @author Jared Erickson
 */
class AddFieldsCommand extends LayerInOutCommand<AddFieldsOptions> {

    @Override
    String getName() {
        "vector addfields"
    }

    @Override
    String getDescription() {
        "Add one or more Fields to the input Layer to create the output Layer"
    }

    @Override
    AddFieldsOptions getOptions() {
        new AddFieldsOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AddFieldsOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, AddFieldsOptions options) {
        layer.schema.addFields(options.fields.collect { f ->
            Field field
            if (f.value.contains("EPSG")) {
                def parts = f.value.split(" ")
                field = new Field(f.key, parts[0], parts[1].startsWith("EPSG") ? parts[1] : "EPSG:${parts[1]}")
            } else {
                field = new Field(f.key, f.value)
            }
            field
        }, getOutputLayerName(layer, "addfields", options))
    }

    static class AddFieldsOptions extends LayerInOutOptions {

        @Option(name = "-f", aliases = "--field", usage = "A Field in the format 'name=type'", required = true)
        Map<String, String> fields

    }
}
