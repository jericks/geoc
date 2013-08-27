package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Subtract one Raster from another Raster.
 * @author Jared Erickson
 */
class SubtractRastersCommand extends RasterInOtherOutCommand<SubtractRastersOptions> {

    @Override
    String getName() {
        "raster subtract"
    }

    @Override
    String getDescription() {
        "Subtract one Raster from another Raster"
    }

    @Override
    SubtractRastersOptions getOptions() {
        new SubtractRastersOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, Raster otherRaster, SubtractRastersOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.minus(otherRaster)
    }

    static class SubtractRastersOptions extends RasterInOtherOutOptions {
    }
}
