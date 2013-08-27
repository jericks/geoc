package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Copy the input Layer to the output Layer
 * @author Jared Erickson
 */
class CopyCommand extends LayerInOutCommand<CopyOptions> {

    @Override
    String getName() {
        "vector copy"
    }

    @Override
    String getDescription() {
        "Copy the input Layer to the output Layer"
    }

    @Override
    CopyOptions getOptions() {
        new CopyOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, CopyOptions options, Reader reader, Writer writer) throws Exception {
        inLayer.getCursor([filter: options.filter, sort: options.sort, start: options.start, max: options.max, fields: options.fields]).each {f->
            outLayer.add(f)
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, CopyOptions options) {
        List fields = layer.schema.fields
        if (options.fields.size() > 0) {
            fields = [layer.schema.geom]
            options.fields.each {name ->
                if (layer.schema.has(name) && !fields.find {Field fld -> fld.name.equalsIgnoreCase(name)}) {
                    fields.add(layer.schema.get(name))
                }
            }
        }
        new Schema(getOutputLayerName(layer, "copy", options), fields)
    }

    static class CopyOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--filter",  usage="The CQL Filter", required = false)
        String filter = null

        @Option(name="-s", aliases="--sort",  usage="The sort Field", required = false)
        List<String> sort = []

        @Option(name="-t", aliases="--start",  usage="The start index", required = false)
        int start = -1

        @Option(name="-m", aliases="--max",  usage="The max number of Features", required = false)
        int max = -1

        @Option(name="-d", aliases="--field",  usage="The sub Field", required = false)
        List<String> fields = []

    }

}
