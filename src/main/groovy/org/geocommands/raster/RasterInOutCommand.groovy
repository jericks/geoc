package org.geocommands.raster

import geoscript.layer.Raster

/**
 * A base class for Commands that take an input Raster and produce an output Raster.
 * @author Jared Erickson
 */
abstract class RasterInOutCommand<T extends RasterInOutOptions> extends RasterCommand<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract Raster createOutputRaster(Raster inRaster, T options, Reader reader, Writer writer) throws Exception

    @Override
    protected void processRaster(Raster inRaster, T options, Reader reader, Writer writer) throws Exception {
        Raster outRaster = createOutputRaster(inRaster, options, reader, writer)
        RasterUtil.writeRaster(outRaster, options, writer)
    }
}
