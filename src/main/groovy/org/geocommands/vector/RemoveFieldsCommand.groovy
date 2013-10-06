package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions
import org.kohsuke.args4j.Option

/**
 * Remove one or more Fields from the input Layer to create the output Layer
 * @author Jared Erickson
 */
class RemoveFieldsCommand extends LayerInOutCommand<RemoveFieldsOptions> {

    @Override
    String getName() {
        "vector removefields"
    }

    @Override
    String getDescription() {
        "Remove one or more Fields from the input Layer to create the output Layer"
    }

    @Override
    RemoveFieldsOptions getOptions() {
        new RemoveFieldsOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, RemoveFieldsOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.eachFeature {Feature f ->
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, RemoveFieldsOptions options) {
        layer.schema.removeFields(options.fields.collect { f ->
            layer.schema.get(f)
        }, getOutputLayerName(layer, "removefields", options))
    }

    static class RemoveFieldsOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--field",  usage="A Field name", required = true)
        List<String> fields

    }
}
