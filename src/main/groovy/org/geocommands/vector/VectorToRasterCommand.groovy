package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.Command
import org.geocommands.raster.RasterUtil

/**
 * The Vector to Raster Command base class
 * @author Jared Erickson
 */
abstract class VectorToRasterCommand<T extends VectorToRasterOptions> extends Command<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract Raster createRaster(Layer layer, T options, Reader reader, Writer writer)

    @Override
    void execute(T options, Reader reader, Writer writer) throws Exception {
        Layer layer = Util.getInputLayer(options.inputWorkspace, options.inputLayer, reader)
        Raster raster = createRaster(layer, options, reader, writer)
        RasterUtil.writeRaster(raster, options.outputFormat, options.outputRaster, writer)
    }
}
