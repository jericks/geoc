package org.geocommands.raster

import geoscript.layer.ArcGrid
import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.MrSID
import geoscript.layer.Raster
import geoscript.layer.WorldImage
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.apache.commons.io.output.WriterOutputStream

/**
 * A collection of Raster Utilities
 * @author Jared Erickson
 */
class RasterUtil {

    /**
     * Get an input Raster either from the RasterOptions or the Reader
     * @param inputRaster The input Raster string
     * @param options The RasterOptions
     * @param reader The Reader
     * @return A Raster (which can be null)
     */
    static Raster getInputRaster(String inputRaster, RasterOptions options, Reader reader) {
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

    /**
     * Get a Raster from the input raster string
     * @param inputRaster The input raster string
     * @param proj A Projection (which can be null)
     * @return A Raster
     */
    static Raster getRaster(String inputRaster, Projection proj) {
        Format format = Format.getFormat(new File(inputRaster))
        format.read(new File(inputRaster), proj)
    }

    static void writeRaster(Raster outRaster, String outputFormat, String outputRaster, Writer writer) {
        if (!outputRaster) {
            new ArcGrid().write(outRaster, new WriterOutputStream(writer))
        } else {
            File file = new File(outputRaster)
            Format format
            if (outputFormat) {
                outputFormat = outputFormat.toLowerCase()
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
                    throw IllegalArgumentException("Unknown Raster Format: '${outputFormat}'!")
                }
            } else {
                format = Format.getFormat(file.absoluteFile)
            }
            format.write(outRaster, file)
        }
    }

    static void writeRaster(Raster outRaster, RasterInOutOptions options, Writer writer) {
        writeRaster(outRaster, options.outputFormat, options.outputRaster, writer)
    }

    /**
     * Dispose of a Raster without throwing an Exception
     * @param raster The Raster (which can be null)
     * @return Did we dispose of the Raster without throwing an Exception?
     */
    static boolean disposeRaster(Raster raster) {
        boolean success = true
        if (raster)  {
            try {
                raster.dispose()
            } catch (Exception e) {
                success = false
            }
        }
        success
    }
}
