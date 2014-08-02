package org.geocommands.raster

import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Reclassify a Raster.
 * @author Jared Erickson
 */
class ReclassifyCommand extends RasterInOutCommand<ReclassifyOptions> {

    @Override
    String getName() {
        "raster reclassify"
    }

    @Override
    String getDescription() {
        "Reclassify a Raster"
    }

    @Override
    ReclassifyOptions getOptions() {
        new ReclassifyOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ReclassifyOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.reclassify(options.ranges.collect { range ->
            int dash = range.indexOf("-")
            int equal = range.indexOf("=")
            def from = range.substring(0, dash).trim() as double
            def to = range.substring(dash + 1, equal).trim() as double
            def value = range.substring(equal + 1).trim() as double
            [min: from, max: to, value: value]
        }, band: options.band, noData: options.noData)
    }

    static class ReclassifyOptions extends RasterInOutOptions {

        @Option(name = "-b", aliases = "--band", usage = "The band", required = false)
        int band = 0

        @Option(name = "-n", aliases = "--nodata", usage = "The NODATA value", required = false)
        double noData = 0

        @Option(name = "-r", aliases = "--range", usage = "A range: from-to=value or 1-10=5", required = true)
        List<String> ranges

    }
}
