package org.geocommands.raster

import geoscript.layer.Raster

/**
 * A Command to Normalize a Raster.
 * @author Jared Erickson
 */
class NormalizeCommand extends RasterInOutCommand<NormalizeOptions> {

    @Override
    String getName() {
        "raster normalize"
    }

    @Override
    String getDescription() {
        "Normalize the values of a Raster"
    }

    @Override
    NormalizeOptions getOptions() {
        new NormalizeOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, NormalizeOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.normalize()
    }

    static class NormalizeOptions extends RasterInOutOptions {
    }
}
