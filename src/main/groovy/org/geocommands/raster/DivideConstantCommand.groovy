package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Divide a constant value to a Raster
 * @author Jared Erickson
 */
class DivideConstantCommand extends RasterInOutCommand<DivideConstantOptions> {

    @Override
    String getName() {
        "raster divide constant"
    }

    @Override
    String getDescription() {
        "Divide a constant value to a Raster"
    }

    @Override
    DivideConstantOptions getOptions() {
        new DivideConstantOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, DivideConstantOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.divide(options.values)
    }

    static class DivideConstantOptions extends RasterInOutOptions {
        @Option(name = "-v", aliases = "--value", usage = "The value", required = true)
        List<Double> values
    }
}
