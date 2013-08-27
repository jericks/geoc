package org.geocommands.raster

import geoscript.style.RasterSymbolizer
import org.geocommands.Command

/**
 * Create a simple Raster SLD
 * @author Jared Erickson
 */
class DefaultStyleCommand extends Command<DefaultStyleOptions> {

    @Override
    String getName() {
        "raster style default"
    }

    @Override
    String getDescription() {
        "Create a simple Raster SLD"
    }

    @Override
    DefaultStyleOptions getOptions() {
        new DefaultStyleOptions()
    }

    @Override
    void execute(DefaultStyleOptions options, Reader reader, Writer writer) throws Exception {
        writer.write(new RasterSymbolizer(options.opacity).sld)
    }

    static class DefaultStyleOptions extends StyleOptions {
    }
}
