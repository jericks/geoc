package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.Raster
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Mosaic a list of rasters together.
 * @author Jared Erickson
 */
class MosaicCommand extends Command<MosaicOptions> {

    @Override
    String getName() {
        "raster mosaic"
    }

    @Override
    String getDescription() {
        "Mosaic a list of rasters together"
    }

    @Override
    MosaicOptions getOptions() {
        new MosaicOptions()
    }

    @Override
    void execute(MosaicOptions options, Reader reader, Writer writer) throws Exception {
        List<Raster> rasters = []
        options.rasters.each { String rasterStr ->
            rasters.add(RasterUtil.getRaster(rasterStr, null, null))
        }
        Raster raster = Raster.mosaic(rasters,
                size: options.size?.split(","),
                bounds: Bounds.fromString(options.bounds)
        )
        RasterUtil.writeRaster(raster, options.outputFormat, options.outputRaster, writer)
    }

    static class MosaicOptions extends Options {

        @Option(name = "-r", aliases = "--raster", usage = "An input Raster", required = true)
        List<String> rasters = []

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-z", aliases = "--size", usage = "The size", required = false)
        String size

        @Option(name = "-o", aliases = "--output-raster", usage = "The output raster", required = false)
        String outputRaster

        @Option(name = "-f", aliases = "--output-raster-format", usage = "The output raster format", required = false)
        String outputFormat

    }
}
