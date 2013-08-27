package org.geocommands.raster

import org.kohsuke.args4j.Option

/**
 * An Options subclass that take an input and output Raster
 * @author Jared Erickson
 */
class RasterInOutOptions extends RasterOptions {

    @Option(name="-o", aliases="--output-raster",  usage="The output raster", required = false)
    String outputRaster

    @Option(name="-f", aliases="--output-raster-format",  usage="The output raster format", required = false)
    String outputFormat
}
