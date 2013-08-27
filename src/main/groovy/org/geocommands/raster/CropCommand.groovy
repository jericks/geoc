package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Crop a Raster
 * @author Jared Erickson
 */
class CropCommand extends RasterInOutCommand<CropOptions> {

    @Override
    String getName() {
        "raster crop"
    }

    @Override
    String getDescription() {
        "Crop a Raster"
    }

    @Override
    CropOptions getOptions() {
        new CropOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, CropOptions options, Reader reader, Writer writer) throws Exception {
        if (options.pixel) {
            Bounds b = Bounds.fromString(options.bounds)
            inRaster.crop(b.minX as int, b.minY as int, b.maxX as int, b.maxY as int)
        } else {
            inRaster.crop(Bounds.fromString(options.bounds))
        }

    }

    static class CropOptions extends RasterInOutOptions {

        @Option(name="-b", aliases="--bound",  usage="The Bounds", required = true)
        String bounds

        @Option(name="-x", aliases="--pixel",  usage="Whether the Bounds is pixel or geographic", required = false)
        boolean pixel

    }
}
