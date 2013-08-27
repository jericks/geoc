package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.MapAlgebraCommand.MapAlgebraOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The MapAlgebraCommand Unit Test
 * @author Jared Erickson
 */
class MapAlgebraCommandTest extends BaseTest {

    @Test
    void executeMultiplyScript() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster", "tif")
        MapAlgebraCommand command = new MapAlgebraCommand()
        MapAlgebraOptions options = new MapAlgebraOptions(
                script: "dest = r1 * r2;",
                rasters: [
                        r1: inFile.absolutePath,
                        r2: otherFile.absolutePath
                ],
                outputRaster: outFile,
                outputProjection: "EPSG:4326"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 12, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 21, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 24, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void executeAddScript() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster", "tif")
        MapAlgebraCommand command = new MapAlgebraCommand()
        MapAlgebraOptions options = new MapAlgebraOptions(
                script: "dest = r1 + r2;",
                rasters: [
                        r1: inFile.absolutePath,
                        r2: otherFile.absolutePath
                ],
                outputRaster: outFile,
                outputProjection: "EPSG:4326"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 6, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 8, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 10, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 10, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runMultiplyScript() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_add", "tif")
        runApp([
                "raster mapalgebra",
                "-s", "dest = r1 * r2;",
                "-r", "r1=" + inFile.getAbsolutePath(),
                "-r", "r2=" + otherFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", "EPSG:4326",
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 12, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 21, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 24, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runAddScript() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_add", "tif")
        runApp([
                "raster mapalgebra",
                "-s", "dest = r1 + r2;",
                "-r", "r1=" + inFile.getAbsolutePath(),
                "-r", "r2=" + otherFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", "EPSG:4326",
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 6, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 8, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 10, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 10, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }
}
