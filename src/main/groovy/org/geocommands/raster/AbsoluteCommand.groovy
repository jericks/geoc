package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Calculate the absolute value of each cell.
 * @author Jared Erickson
 */
class AbsoluteCommand extends RasterInOutCommand<AbsoluteOptions> {

    @Override
    String getName() {
        "raster abs"
    }

    @Override
    String getDescription() {
        "Calculate the absolute value for each cell."
    }

    @Override
    AbsoluteOptions getOptions() {
        new AbsoluteOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, AbsoluteOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.absolute()
    }

    static class AbsoluteOptions extends RasterInOutOptions {
    }
}
