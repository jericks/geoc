package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Invert the values of a Raster
 * @author Jared Erickson
 */
class InvertCommand extends RasterInOutCommand<InvertOptions> {

    @Override
    String getName() {
        "raster invert"
    }

    @Override
    String getDescription() {
        "Invert the values of a Raster"
    }

    @Override
    InvertOptions getOptions() {
        new InvertOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, InvertOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.invert()
    }

    static class InvertOptions extends RasterInOutOptions {
    }
}
