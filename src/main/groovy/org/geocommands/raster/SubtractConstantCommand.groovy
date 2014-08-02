package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Substract a constant value to a Raster
 * @author Jared Erickson
 */
class SubtractConstantCommand extends RasterInOutCommand<SubtractConstantOptions> {

    @Override
    String getName() {
        "raster subtract constant"
    }

    @Override
    String getDescription() {
        "Substract a constant value to a Raster"
    }

    @Override
    SubtractConstantOptions getOptions() {
        new SubtractConstantOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, SubtractConstantOptions options, Reader reader, Writer writer) throws Exception {
        if (options.from) {
            inRaster.minusFrom(options.values)
        } else {
            inRaster.minus(options.values)
        }
    }

    static class SubtractConstantOptions extends RasterInOutOptions {
        @Option(name = "-v", aliases = "--value", usage = "The value", required = true)
        List<Double> values

        @Option(name = "-m", aliases = "--from", usage = "Whether to subtract the Raster from the constant or vice verse", required = false)
        boolean from = false
    }
}
