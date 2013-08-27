package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Add a constant value to a Raster
 */
class AddConstantCommand extends RasterInOutCommand <AddConstantOptions> {

    @Override
    String getName() {
        "raster add constant"
    }

    @Override
    String getDescription() {
        "Add a constant value to a Raster"
    }

    @Override
    AddConstantOptions getOptions() {
        new AddConstantOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, AddConstantOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.add(options.values)
    }

    static class AddConstantOptions extends RasterInOutOptions {
        @Option(name="-v", aliases="--value",  usage="The value", required = true)
        List<Double> values
    }
}
