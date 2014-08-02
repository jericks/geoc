package org.geocommands.vector

import geoscript.feature.Field
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Update the values of a Layer's Field
 * @author Jared Erickson
 */
class UpdateFieldCommand extends LayerCommand<UpdateFieldOptions> {

    @Override
    String getName() {
        "vector updatefield"
    }

    @Override
    String getDescription() {
        "Update the values of a Layer's Field"
    }

    @Override
    UpdateFieldOptions getOptions() {
        new UpdateFieldOptions()
    }

    @Override
    protected void processLayer(Layer layer, UpdateFieldOptions options, Reader reader, Writer writer) throws Exception {
        Field fld = layer.schema.get(options.field)
        def value = !options.script ? Expression.fromCQL(options.value) : options.value
        layer.update(fld, value, options.filter, options.script)
    }

    @Override
    protected boolean shouldWriteLayer() {
        true
    }

    static class UpdateFieldOptions extends LayerOptions {

        @Option(name = "-d", aliases = "--field", usage = "The Field name", required = true)
        String field

        @Option(name = "-v", aliases = "--value", usage = "The value", required = true)
        String value

        @Option(name = "-f", aliases = "--filter", usage = "The CQL Filter", required = false)
        String filter = "INCLUDE"

        @Option(name = "-s", aliases = "--script", usage = "Whether the value is a script or not", required = false)
        boolean script
    }
}
