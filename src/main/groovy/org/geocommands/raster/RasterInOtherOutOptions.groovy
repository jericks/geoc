package org.geocommands.raster

import org.kohsuke.args4j.Option

/**
 *  Options for Commands that read in two Raster and write a Raster
 *  @author Jared Erickson
 */
class RasterInOtherOutOptions extends RasterInOutOptions {

    @Option(name="-k", aliases="--other-raster",  usage="The other raster", required = true)
    String otherRaster

    @Option(name="-j", aliases="--other-projection",  usage="The other projection", required = false)
    String otherProjection
}
