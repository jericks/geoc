package org.geocommands.raster

import geoscript.layer.Raster
import org.geocommands.Command

/**
 * An abstract Command subclass that operates on a Raster.
 * @author Jared Erickson
 */
abstract class RasterCommand<T extends RasterOptions> extends Command<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    void execute(T options, Reader reader, Writer writer) throws Exception {
        Raster raster = RasterUtil.getInputRaster(options.inputRaster, options, reader)
        try {
            processRaster(raster, options, reader, writer)
            if (shouldWriteRaster()) {
                RasterUtil.writeRaster(raster, writer)
            }
        }
        finally {
            raster.dispose()
        }
    }

    protected abstract void processRaster(Raster raster, T options, Reader reader, Writer writer) throws Exception

    protected boolean shouldWriteRaster() {
        false
    }
}
