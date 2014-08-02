package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Append the Features from an other Layer to the input Layer
 * @author Jared Erickson
 */
class AppendCommand extends LayerCommand<AppendOptions> {

    @Override
    String getName() {
        "vector append"
    }

    @Override
    String getDescription() {
        "Append the Features from an other Layer to the input Layer"
    }

    @Override
    AppendOptions getOptions() {
        new AppendOptions()
    }

    @Override
    protected boolean shouldWriteLayer() {
        true
    }

    @Override
    protected void processLayer(Layer layer, AppendOptions options, Reader reader, Writer writer) throws Exception {
        Layer otherLayer = Util.getOtherLayer(options.otherWorkspace, options.otherLayer)
        layer.withWriter { geoscript.layer.Writer w ->
            otherLayer.eachFeature { f ->
                w.add(f)
            }
        }
    }

    static class AppendOptions extends LayerOptions {

        @Option(name = "-k", aliases = "--other-workspace", usage = "The other workspace", required = true)
        String otherWorkspace

        @Option(name = "-y", aliases = "--other-layer", usage = "The other layer", required = false)
        String otherLayer

    }
}
