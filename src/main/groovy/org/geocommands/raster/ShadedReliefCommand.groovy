package org.geocommands.raster

import geoscript.layer.Raster
import org.geocommands.raster.ShadedReliefCommand.ShadedReliefOptions
import org.kohsuke.args4j.Option

class ShadedReliefCommand extends RasterInOutCommand<ShadedReliefOptions> {

    @Override
    String getName() {
        "raster shadedrelief"
    }

    @Override
    String getDescription() {
        "Create a shaded relief raster"
    }

    @Override
    ShadedReliefOptions getOptions() {
        new ShadedReliefOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ShadedReliefOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.createShadedRelief(
                options.scale,
                options.altitude,
                options.azimuth,
                resX: options.resX,
                resY: options.resY,
                zetaFactory: options.zetaFactory,
                algorithm: options.algorithm
        )
    }

    static class ShadedReliefOptions extends RasterInOutOptions {

        @Option(name = "-s", aliases = "--scale", usage = "The scale", required = true)
        double scale

        @Option(name = "-a", aliases = "--altitude", usage = "The altitude", required = true)
        double altitude

        @Option(name = "-m", aliases = "--azimuth", usage = "The azimuth", required = true)
        double azimuth

        @Option(name = "-x", aliases = "--resx", usage = "The x resolution", required = false)
        double resX = 0.5d

        @Option(name = "-y", aliases = "--resy", usage = "The y resolution", required = false)
        double resY = 0.5d

        @Option(name = "-z", aliases = "--zeta-factory", usage = "The zeta factory", required = false)
        double zetaFactory = 1.0d

        @Option(name = "-g", aliases = "--algorithm", usage = "The algorithm", required = false)
        String algorithm = "DEFAULT"

    }

}
