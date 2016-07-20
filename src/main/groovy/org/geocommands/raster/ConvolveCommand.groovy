package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * A Command to Convolve a Raster.
 * @author Jared Erickson
 */
class ConvolveCommand extends RasterInOutCommand<ConvolveOptions> {

    @Override
    String getName() {
        "raster convolve"
    }

    @Override
    String getDescription() {
        "Convolve the values of a Raster"
    }

    @Override
    ConvolveOptions getOptions() {
        new ConvolveOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ConvolveOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.convolve(options.width, options.height)
    }

    static class ConvolveOptions extends RasterInOutOptions {

        @Option(name = "-w", aliases = "--width", usage = "The kernel width", required = true)
        int width

        @Option(name = "-h", aliases = "--height", usage = "The kernel height", required = true)
        int height

    }
}
