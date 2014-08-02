package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Delete features from a Layer in place
 * @author Jared Erickson
 */
class DeleteCommand extends LayerCommand<DeleteOptions> {

    @Override
    String getName() {
        "vector delete"
    }

    @Override
    String getDescription() {
        "Delete features from a Layer in place"
    }

    @Override
    DeleteOptions getOptions() {
        new DeleteOptions()
    }

    @Override
    protected void processLayer(Layer layer, DeleteOptions options, Reader reader, Writer writer) throws Exception {
        layer.delete(options.filter)
    }

    @Override
    protected boolean shouldWriteLayer() {
        true
    }

    static class DeleteOptions extends LayerOptions {
        @Option(name = "-f", aliases = "--filter", usage = "The CQL Filter", required = true)
        String filter
    }
}
