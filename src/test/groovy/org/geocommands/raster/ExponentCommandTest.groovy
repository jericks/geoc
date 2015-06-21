package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.raster.ExponentCommand.ExponentOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * The ExponentCommand Unit Test
 * @author Jared Erickson
 */
class ExponentCommandTest extends BaseTest {

    @Test void execute() {
        ExponentCommand cmd = new ExponentCommand()
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_exponent", "tif")
        ExponentOptions options = new ExponentOptions(
                inputRaster: inFile.absolutePath,
                outputRaster: outFile.absolutePath
        )
        cmd.execute(options)
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(255, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_exponent", "tif")
        runApp([
                "raster exp",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ],"")
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(255, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }
}
