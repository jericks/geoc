package org.geocommands.raster

import geoscript.layer.ArcGrid
import geoscript.layer.Format
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.apache.commons.io.output.WriterOutputStream
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
        Raster raster = getInputRaster(options.inputRaster, options, reader)
        try {
            processRaster(raster, options, reader, writer)
            if (shouldWriteRaster()) {
                new ArcGrid().write(raster, new WriterOutputStream(writer))
            }
        }
        finally {
            raster.dispose()
        }
    }

    protected abstract void processRaster(Raster raster, T options, Reader reader, Writer writer) throws Exception;

    protected boolean shouldWriteRaster() {
        false
    }

    Raster getInputRaster(String inputRaster, T options, Reader reader) {
        Raster raster = null
        Projection proj = options.inputProjection ? new Projection(options.inputProjection) : null
        if (inputRaster) {
            Format format = Format.getFormat(new File(inputRaster))
            raster = format.read(new File(inputRaster), proj)
        } else {
            // Assume that Rasters from Standard Input are in EPSG:4326
            if (!proj) {
                proj = new Projection("EPSG:4326")
            }
            raster = new ArcGrid().read(new ReaderInputStream(reader), proj)
        }
        raster
    }

}
