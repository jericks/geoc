package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Multiply a constant value to a Raster
 * @author Jared Erickson
 */
class MultiplyConstantCommand  extends RasterInOutCommand <MultiplyConstantOptions> {

    @Override
    String getName() {
        "raster multiply constant"
    }

    @Override
    String getDescription() {
        "Multiply a constant value to a Raster"
    }

    @Override
    MultiplyConstantOptions getOptions() {
        new MultiplyConstantOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, MultiplyConstantOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.multiply(options.values)
    }

    static class MultiplyConstantOptions extends RasterInOutOptions {
        @Option(name="-v", aliases="--value",  usage="The value", required = true)
        List<Double> values
    }
}
