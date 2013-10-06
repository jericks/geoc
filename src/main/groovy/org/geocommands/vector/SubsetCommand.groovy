package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Extract a subset of Features from the input Layer
 * @author Jared Erickson
 */
class SubsetCommand extends LayerInOutCommand<SubsetOptions> {

    @Override
    String getName() {
        "vector subset"
    }

    @Override
    String getDescription() {
        "Extract a subset of Features from the input Layer"
    }

    @Override
    SubsetOptions getOptions() {
        new SubsetOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, SubsetOptions options, Reader reader, Writer writer) throws Exception {
        int start = options.start
        int max = options.max
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.getCursor(options.filter, options.sort, max, start).each{Feature f ->
                w.add(f)
            }
        }
    }

    static class SubsetOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--filter",  usage="The CQL Filter", required = false)
        String filter

        @Option(name="-s", aliases="--sort",  usage="The sort field", required = false)
        List<String> sort

        @Option(name="-m", aliases="--max",  usage="The maximum number of Features to include", required = false)
        int max = -1

        @Option(name="-t", aliases="--start",  usage="The index of the Feature to start at", required = false)
        int start = -1

    }

}
