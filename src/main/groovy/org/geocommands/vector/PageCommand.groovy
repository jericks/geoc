package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Page through Feature in the input Layer
 * @author Jared Erickson
 */
class PageCommand extends LayerInOutCommand<PageOptions> {

    @Override
    String getName() {
        "vector page"
    }

    @Override
    String getDescription() {
        "Page through Feature in the input Layer"
    }

    @Override
    PageOptions getOptions() {
        new PageOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, PageOptions options, Reader reader, Writer writer) throws Exception {
        int start = options.start
        int max = options.max
        // @TODO Remove this when upgrading beyond GeoScript Groovy 1.1 which has a bug in Cursor related to pagings
        // https://github.com/jericks/geoscript-groovy/commit/037fd9169f02c2035e56728dbb24c83870ae14eb
        if (!inLayer.fs.queryCapabilities.offsetSupported && max > -1 && start > -1) {
            max = max - start
        }
        outLayer.add(inLayer.getCursor(start: start, max: max).collect())
    }

    static class PageOptions extends LayerInOutOptions {

        @Option(name="-m", aliases="--max",  usage="The maximum number of Features to include", required = false)
        int max = -1

        @Option(name="-t", aliases="--start",  usage="The index of the Feature to start at", required = false)
        int start = -1

    }
}
