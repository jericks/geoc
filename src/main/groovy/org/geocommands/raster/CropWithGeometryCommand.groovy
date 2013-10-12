package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Crop a Raster with a Geometry
 * @author Jared Erickson
 */
class CropWithGeometryCommand extends RasterInOutCommand<CropWithGeometryOptions> {

    @Override
    String getName() {
        "raster crop geometry"
    }

    @Override
    String getDescription() {
        "Crop a Raster with a Geometry"
    }

    @Override
    CropWithGeometryOptions getOptions() {
        new CropWithGeometryOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, CropWithGeometryOptions options, Reader reader, Writer writer) throws Exception {
        Geometry g = Geometry.fromString(options.geometry)
        inRaster.crop(g)
    }

    static class CropWithGeometryOptions extends RasterInOutOptions {

        @Option(name="-g", aliases="--geometry",  usage="The Geometry", required = true)
        String geometry

    }
}
