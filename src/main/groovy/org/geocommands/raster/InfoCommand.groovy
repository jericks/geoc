package org.geocommands.raster

import geoscript.layer.Band
import geoscript.layer.Raster

/**
 * A Command to get basic information from a Raster.
 * http://www.gdal.org/gdalinfo.html
 * @author Jared Erickson
 */
class InfoCommand extends RasterCommand<InfoOptions> {


    @Override
    String getName() {
        "raster info"
    }

    @Override
    String getDescription() {
        "Get information about a Raster"
    }

    @Override
    InfoOptions getOptions() {
        new InfoOptions()
    }

    @Override
    protected void processRaster(Raster raster, InfoOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        writer.write("Format: ${raster.format ? raster.format.name : 'Unknown'}")
        writer.write(NEW_LINE)
        writer.write("Size: ${raster.size[0]}, ${raster.size[1]}")
        writer.write(NEW_LINE)
        writer.write("Projection ID: ${raster.proj != null ? raster.proj.id : 'Unknown'}")
        writer.write(NEW_LINE)
        writer.write("Projection WKT: ${raster.proj != null ? raster.proj.wkt : 'Unknown'}")
        writer.write(NEW_LINE)
        writer.write("Extent: ${raster.bounds.minX}, ${raster.bounds.minY}, ${raster.bounds.maxX}, ${raster.bounds.maxY}")
        writer.write(NEW_LINE)
        writer.write("Pixel Size: ${raster.pixelSize[0]}, ${raster.pixelSize[1]}")
        writer.write(NEW_LINE)
        writer.write("Block Size: ${raster.blockSize[0]}, ${raster.blockSize[1]}")
        writer.write(NEW_LINE)
        Map extrema = raster.extrema
        writer.write("Bands:")
        raster.bands.eachWithIndex { Band b, int i ->
            writer.write(NEW_LINE)
            writer.write("   ${b}")
            writer.write(NEW_LINE)
            writer.write("      Min Value: ${extrema.min[i]} Max Value: ${extrema.max[i]}")
        }
    }

    static class InfoOptions extends RasterOptions {
    }
}
