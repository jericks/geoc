package org.geocommands.vector

import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * The base class for Options that convert a vector Layer to a Raster
 */
class VectorToRasterOptions extends Options {

    @Option(name="-i", aliases="--input-workspace",  usage="The input workspace", required = false)
    String inputWorkspace

    @Option(name="-l", aliases="--input-layer",  usage="The input layer", required = false)
    String inputLayer

    @Option(name="-o", aliases="--output-raster",  usage="The output raster", required = false)
    String outputRaster

    @Option(name="-f", aliases="--output-raster-format",  usage="The output raster format", required = false)
    String outputFormat
}
