package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.raster.LogCommand.LogOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The LogCommand Unit Test
 * @author Jared Erickson
 */
class LogCommandTest extends BaseTest {

    @Test void execute() {
        LogCommand cmd = new LogCommand()
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_log", "tif")
        LogOptions options = new LogOptions(
                inputRaster: inFile.absolutePath,
                outputRaster: outFile.absolutePath
        )
        cmd.execute(options)
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(5, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(6, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_log", "tif")
        runApp([
            "raster log",
            "-i", inFile.absolutePath,
            "-o", outFile.absolutePath
        ],"")
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(5, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(6, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(5, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }
}
