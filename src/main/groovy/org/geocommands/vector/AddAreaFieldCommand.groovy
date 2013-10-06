package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add an area field to a Layer and set the values.
 * @author Jared Erickson
 */
class AddAreaFieldCommand extends LayerInOutCommand<AddAreaFieldOptions> {

    @Override
    String getName() {
        "vector addareafield"
    }

    @Override
    String getDescription() {
        "Add an area Field"
    }

    @Override
    AddAreaFieldOptions getOptions() {
        new AddAreaFieldOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AddAreaFieldOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.eachFeature {Feature f ->
                Map attributes = f.attributes
                attributes[options.areaFieldName] = f.geom.area
                w.add(outLayer.schema.feature(attributes, f.id))
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, AddAreaFieldOptions options) {
        layer.schema.addField(new Field(options.areaFieldName, "double"), getOutputLayerName(layer, "area", options))
    }

    static class AddAreaFieldOptions extends LayerInOutOptions {
        @Option(name="-f", aliases="--area-fieldname",  usage="The name for the area Field", required = false)
        String areaFieldName = "AREA"
    }
}
