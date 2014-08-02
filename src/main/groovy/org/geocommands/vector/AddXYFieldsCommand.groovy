package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Point
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add a xy fields to a Layer and set the values.
 * @author Jared Erickson
 */
class AddXYFieldsCommand extends LayerInOutCommand<AddXYFieldsOptions> {

    @Override
    String getName() {
        "vector addxyfields"
    }

    @Override
    String getDescription() {
        "Add a XY Fields"
    }

    @Override
    AddXYFieldsOptions getOptions() {
        new AddXYFieldsOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AddXYFieldsOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Point pt = f.geom.centroid
                Map attributes = f.attributes
                attributes[options.xFieldName] = pt.x
                attributes[options.yFieldName] = pt.y
                w.add(outLayer.schema.feature(attributes, f.id))
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, AddXYFieldsOptions options) {
        layer.schema.addFields([
                new Field(options.xFieldName, "double"),
                new Field(options.yFieldName, "double")
        ], getOutputLayerName(layer, "xyfield", options))
    }

    static class AddXYFieldsOptions extends LayerInOutOptions {

        @Option(name = "-x", aliases = "--x-fieldname", usage = "The name for the X Field", required = false)
        String xFieldName = "X"

        @Option(name = "-y", aliases = "--y-fieldname", usage = "The name for the Y Field", required = false)
        String yFieldName = "Y"

    }
}
