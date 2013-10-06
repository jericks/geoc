package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Sort the Features in the input Layer.
 * @author Jared Erickson
 */
class SortCommand extends LayerInOutCommand<SortOptions> {

    @Override
    String getName() {
        "vector sort"
    }

    @Override
    String getDescription() {
        "Sort the Features in the input Layer."
    }

    @Override
    SortOptions getOptions() {
        new SortOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, SortOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.getCursor(sort: options.sort).each {Feature f ->
                w.add(f)
            }
        }
    }

    static class SortOptions extends LayerInOutOptions {

        @Option(name="-s", aliases="--sort",  usage="The sort field", required = true)
        List<String> sort

    }
}
