package org.geocommands.raster

import org.kohsuke.args4j.Option

/**
 * A base class for Raster to Vector Commands
 * @author Jared Erickson
 */
class RasterToVectorOptions extends RasterOptions {

    @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
    String outputWorkspace

    @Option(name = "-r", aliases = "--output-layer", usage = "The output layer", required = false)
    String outputLayer

}
