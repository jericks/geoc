package org.geocommands.raster

import geoscript.layer.Raster

/**
 * Add two Rasters together.
 * @author Jared Erickson
 */
class AddRastersCommand extends RasterInOtherOutCommand<AddRastersOptions> {

    @Override
    String getName() {
        "raster add"
    }

    @Override
    String getDescription() {
        "Add two Rasters together"
    }

    @Override
    AddRastersOptions getOptions() {
        new AddRastersOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, Raster otherRaster, AddRastersOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.add(otherRaster)
    }

    static class AddRastersOptions extends RasterInOtherOutOptions {
    }
}
