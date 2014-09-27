package org.geocommands.raster

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.vector.Util

/**
 * The base class for Raster To Vector Commands
 * @author Jared Erickson
 */
abstract class RasterToVectorCommand<T extends RasterToVectorOptions> extends RasterCommand<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract void convertRasterToVector(Raster raster, Layer layer, T options, Reader reader, Writer writer) throws Exception

    protected void processRaster(Raster raster, T options, Reader reader, Writer writer) throws Exception {
        Layer outLayer = getOutputLayer(raster, options)
        try {
            convertRasterToVector(raster, outLayer, options, reader, writer)
            if (outLayer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
        }
        finally {
            outLayer.workspace.close()
        }
    }

    protected Schema createOutputSchema(Raster raster, T options) {
        new Schema(Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, name.replaceAll(" ", "_")), [new Field("the_geom", "polygon")])
    }

    Layer getOutputLayer(Raster raster, T options) {
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = Workspace.getWorkspace(options.outputWorkspace)
        }
        workspace.create(createOutputSchema(raster, options))
    }
}
