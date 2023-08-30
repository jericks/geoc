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
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.getCursor(start: start, max: max).each { f ->
                w.add(f)
            }
        }
    }

    static class PageOptions extends LayerInOutOptions {

        @Option(name = "-m", aliases = "--max", usage = "The maximum number of Features to include", required = false)
        int max = -1

        @Option(name = "-t", aliases = "--start", usage = "The 0 based index of the Feature to start at", required = false)
        int start = -1

    }
}
