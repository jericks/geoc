package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Multiply two Rasters together.
 * @author Jared Erickson
 */
class MultiplyRastersCommand extends RasterInOtherOutCommand<MultiplyRastersOptions> {

    @Override
    String getName() {
        "raster multiply"
    }

    @Override
    String getDescription() {
        "Multiply two Rasters together"
    }

    @Override
    MultiplyRastersOptions getOptions() {
        new MultiplyRastersOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, Raster otherRaster, MultiplyRastersOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.multiply(otherRaster)
    }

    static class MultiplyRastersOptions extends RasterInOtherOutOptions {
    }
}
