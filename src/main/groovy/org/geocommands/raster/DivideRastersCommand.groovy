package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Divide one Raster by another Raster.
 * @author Jared Erickson
 */
class DivideRastersCommand extends RasterInOtherOutCommand<DivideRastersOptions> {

    @Override
    String getName() {
        "raster divide"
    }

    @Override
    String getDescription() {
        "Divide one Raster by another Raster"
    }

    @Override
    DivideRastersOptions getOptions() {
        new DivideRastersOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, Raster otherRaster, DivideRastersOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.divide(otherRaster)
    }

    static class DivideRastersOptions extends RasterInOtherOutOptions {
    }
}
