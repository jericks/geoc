package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Resample a Raster
 * @author Jared Erickson
 */
class ResampleCommand extends RasterInOutCommand<ResampleOptions>{

    @Override
    String getName() {
        "raster resample"
    }

    @Override
    String getDescription() {
        "Resample a Raster"
    }

    @Override
    ResampleOptions getOptions() {
        new ResampleOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ResampleOptions options, Reader reader, Writer writer) throws Exception {
        Map resampleOptions = [:]
        if (options.bounds) {
            resampleOptions.bbox = Bounds.fromString(options.bounds)
            if (resampleOptions.bbox && !resampleOptions.bbox.proj) {
                resampleOptions.bbox.setProj(inRaster.proj)
            }
        }
        if (options.size) {
            resampleOptions.size = (options.size.contains(",") ? options.size.split(",") : options.size.split(" ")).collect{it as int}
        }
        inRaster.resample(resampleOptions)
    }

    static class ResampleOptions extends RasterInOutOptions {

        @Option(name="-b", aliases="--bounds",  usage="The bounding box", required = false)
        String bounds

        @Option(name="-s", aliases="--size",  usage="The output size", required = false)
        String size
    }
}
