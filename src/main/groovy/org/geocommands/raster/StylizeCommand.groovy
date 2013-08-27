package org.geocommands.raster

import geoscript.layer.Raster
import geoscript.style.io.SLDReader
import org.kohsuke.args4j.Option

/**
 * Create a new Raster by baking the style into an existing Raster
 */
class StylizeCommand extends RasterInOutCommand<StylizeOptions>{

    @Override
    String getName() {
        "raster stylize"
    }

    @Override
    String getDescription() {
        "Create a new Raster by baking the style into an existing Raster"
    }

    @Override
    StylizeOptions getOptions() {
        new StylizeOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, StylizeOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.stylize(new SLDReader().read(options.sld))
    }

    static class StylizeOptions extends RasterInOutOptions {
        @Option(name="-s", aliases="--style",  usage="The SLD style file", required = true)
        File sld
    }
}
