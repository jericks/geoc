package org.geocommands.raster

import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * An Options subclass for Command that read in a Raster
 * @author Jared Erickson
 */
class RasterOptions extends Options {

    @Option(name="-i", aliases="--input-raster",  usage="The input raster", required = false)
    String inputRaster

    @Option(name="-l", aliases="--input-raster-name",  usage="The input raster name", required = false)
    String inputRasterName

    @Option(name="-p", aliases="--input-projection",  usage="The input projection", required = false)
    String inputProjection

}
