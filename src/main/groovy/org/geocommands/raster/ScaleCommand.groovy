package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Scale a Raster
 * @author Jared Erickson
 */
class ScaleCommand extends RasterInOutCommand<ScaleOptions> {

    @Override
    String getName() {
        "raster scale"
    }

    @Override
    String getDescription() {
        "Scale a Raster"
    }

    @Override
    ScaleOptions getOptions() {
        new ScaleOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ScaleOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.scale(options.x, options.y, options.xTrans, options.yTrans, options.interpolation)
    }

    static class ScaleOptions extends RasterInOutOptions {

        @Option(name="-x", aliases="--x-scale",  usage="The scale factor along the x axis", required = true)
        float x

        @Option(name="-y", aliases="--y-scale",  usage="The scale factor along the y axis", required = true)
        float y

        @Option(name="-t", aliases="--x-translate",  usage="The x translation", required = false)
        float xTrans = 0

        @Option(name="-r", aliases="--y-translate",  usage="The y translation", required = false)
        float yTrans = 0

        @Option(name="-l", aliases="--interpolation",  usage="The interpolation method (bicubic, bicubic2, bilinear, nearest)", required = false)
        String interpolation = "nearest"

    }
}
