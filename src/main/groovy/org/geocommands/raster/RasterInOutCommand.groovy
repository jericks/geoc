package org.geocommands.raster

import geoscript.layer.ArcGrid
import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.MrSID
import geoscript.layer.Raster
import geoscript.layer.WorldImage
import org.apache.commons.io.output.WriterOutputStream

/**
 * A base class for Commands that take an input Raster and produce an output Raster.
 * @author Jared Erickson
 */
abstract class RasterInOutCommand<T extends RasterInOutOptions> extends RasterCommand<T>{

    abstract String getName()
    abstract String getDescription()
    abstract T getOptions()

    abstract Raster createOutputRaster(Raster inRaster, T options, Reader reader, Writer writer) throws Exception;

    @Override
    protected void processRaster(Raster inRaster, T options, Reader reader, Writer writer) throws Exception {
        Raster outRaster = createOutputRaster(inRaster, options, reader, writer)
        if (!options.outputRaster) {
            new ArcGrid().write(outRaster, new WriterOutputStream(writer))
        } else {
            File file = new File(options.outputRaster)
            Format format
            if (options.outputFormat) {
                String outputFormat = options.outputFormat.toLowerCase()
                if (outputFormat in ["geotif"]) {
                    format = new GeoTIFF()
                } else if (outputFormat in ["worldimage","png","jpeg","jpg","gif"]) {
                    format = new WorldImage()
                } else if (outputFormat in ["mrsid","sid"]) {
                    format = new MrSID()
                } else if (outputFormat in ["arcgrid","asc"]) {
                    format = new ArcGrid()
                }
                // @TODO More Formats
                else {
                    throw IllegalArgumentException("Unknown Raster Format: '${options.outputFormat}'!")
                }
            } else {
                format = Format.getFormat(file.absoluteFile)
            }
            format.write(outRaster, file)
        }
    }
}
