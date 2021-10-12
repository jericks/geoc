package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.BaseTest
import org.geocommands.raster.AbsoluteCommand.AbsoluteOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The AbsoluteCommand Unit Test
 * @author Jared Erickson
 */
class AbsoluteCommandTest extends BaseTest {

    @Test void execute() {
        AbsoluteCommand cmd = new AbsoluteCommand()
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_absolute", "tif")
        AbsoluteOptions options = new AbsoluteOptions(
                inputRaster: inFile.absolutePath,
                outputRaster: outFile.absolutePath
        )
        cmd.execute(options)
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(234, outRaster.getValue(new Point(-120,  47), 0), 0.1)
        assertEquals(177, outRaster.getValue(new Point( 120,  45), 0), 0.1)
        assertEquals(231, outRaster.getValue(new Point(   0, -45), 0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(   0,  45), 0), 0.1)
        assertEquals(188, outRaster.getValue(new Point(  40, -45), 0), 0.1)
        assertEquals(202, outRaster.getValue(new Point(  40,  45), 0), 0.1)
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_absolute", "tif")
        runApp([
                "raster abs",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ],"")
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(234, outRaster.getValue(new Point(-120,  47), 0), 0.1)
        assertEquals(177, outRaster.getValue(new Point( 120,  45), 0), 0.1)
        assertEquals(231, outRaster.getValue(new Point(   0, -45), 0), 0.1)
        assertEquals(255, outRaster.getValue(new Point(   0,  45), 0), 0.1)
        assertEquals(188, outRaster.getValue(new Point(  40, -45), 0), 0.1)
        assertEquals(202, outRaster.getValue(new Point(  40,  45), 0), 0.1)
    }
}
