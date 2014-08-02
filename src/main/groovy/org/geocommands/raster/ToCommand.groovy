package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Convert a Raster from one format to another
 * @author Jared Erickson
 */
class ToCommand extends RasterInOutCommand<ToOptions> {

    @Override
    String getName() {
        "raster to"
    }

    @Override
    String getDescription() {
        "Convert a Raster from one format to another"
    }

    @Override
    ToOptions getOptions() {
        new ToOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ToOptions options, Reader reader, Writer writer) throws Exception {
        inRaster
    }

    static class ToOptions extends RasterInOutOptions {
    }
}
