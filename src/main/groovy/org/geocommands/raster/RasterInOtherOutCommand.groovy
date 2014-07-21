package org.geocommands.raster

import geoscript.layer.Raster
import geoscript.proj.Projection
import org.geocommands.Command

/**
 * A base class for Raster Commands that read in two Raster and write a Raster.
 * @author Jared Erickson
 */
abstract class RasterInOtherOutCommand <T extends RasterInOtherOutOptions> extends Command<T> {

    abstract String getName()
    abstract String getDescription()
    abstract T getOptions()

    void execute(T options, Reader reader, Writer writer) throws Exception {
        Raster inRaster = RasterUtil.getInputRaster(options.inputRaster, options, reader)
        Raster otherRaster = RasterUtil.getRaster(options.otherRaster, options.otherRasterName, options.otherProjection ? new Projection(options.otherProjection) : null)
        Raster outRaster
        try {
            outRaster = createOutputRaster(inRaster, otherRaster, options, reader, writer)
            RasterUtil.writeRaster(outRaster, options, writer)
        }
        finally {
            RasterUtil.disposeRaster(inRaster)
            RasterUtil.disposeRaster(otherRaster)
            RasterUtil.disposeRaster(outRaster)
        }
    }

    abstract Raster createOutputRaster(Raster inRaster, Raster otherRaster, T options, Reader reader, Writer writer) throws Exception;

}
