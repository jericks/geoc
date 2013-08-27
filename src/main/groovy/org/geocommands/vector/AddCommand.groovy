package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Add a Feature to an existing Layer
 * @author Jared Erickson
 */
class AddCommand extends LayerCommand<AddOptions>{

    @Override
    String getName() {
        "vector add"
    }

    @Override
    String getDescription() {
        "Add a Feature to an existing Layer"
    }

    @Override
    AddOptions getOptions() {
        new AddOptions()
    }

    @Override
    protected boolean shouldWriteLayer() {
        true
    }

    @Override
    protected void processLayer(Layer layer, AddOptions options, Reader reader, Writer writer) throws Exception {
        layer.add(options.values)
    }

    static class AddOptions extends LayerOptions {
        @Option(name="-v", aliases="--value",  usage="A value 'field=value'", required = true)
        Map<String,String> values
    }
}
