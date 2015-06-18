package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Calculate the log of each cell.
 * @author Jared Erickson
 */
class LogCommand extends RasterInOutCommand<LogOptions> {

    @Override
    String getName() {
        "raster log"
    }

    @Override
    String getDescription() {
        "Calculate the log for each cell."
    }

    @Override
    LogOptions getOptions() {
        new LogOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, LogOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.log()
    }

    static class LogOptions extends RasterInOutOptions {
    }
}
