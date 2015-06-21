package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Calculate the exponent of each cell.
 * @author Jared Erickson
 */
class ExponentCommand extends RasterInOutCommand<ExponentOptions> {

    @Override
    String getName() {
        "raster exp"
    }

    @Override
    String getDescription() {
        "Calculate the exponent for each cell."
    }

    @Override
    ExponentOptions getOptions() {
        new ExponentOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ExponentOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.exp()
    }

    static class ExponentOptions extends RasterInOutOptions {
    }
}
