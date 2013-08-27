package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.SubtractRastersCommand.SubtractRastersOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The SubtractRastersCommand Unit Test
 * @author Jared Erickson
 */
class SubtractRastersCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_subtract", "tif")
        SubtractRastersCommand command = new SubtractRastersCommand()
        SubtractRastersOptions options = new SubtractRastersOptions(
                inputRaster: inFile,
                inputProjection: "EPSG:4326",
                otherRaster: otherFile,
                otherProjection: "EPSG:4326",
                outputRaster: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 4, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 2, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster1.acs")
        File otherFile = getResource("raster2.acs")
        StringWriter writer = new StringWriter()
        SubtractRastersCommand command = new SubtractRastersCommand()
        SubtractRastersOptions options = new SubtractRastersOptions(
                inputProjection: "EPSG:4326",
                otherRaster: otherFile,
                otherProjection: "EPSG:4326"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(writer.toString())), new Projection("EPSG:4326"))

        assertEquals 4, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 2, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_subtract", "tif")
        runApp([
                "raster subtract",
                "-i", inFile.absolutePath,
                "-p", "EPSG:4326",
                "-k", otherFile.absolutePath,
                "-j", "EPSG:4326",
                "-o", outFile.absolutePath
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)

        assertEquals 4, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 2, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        File otherFile = getResource("raster2.acs")
        String result = runApp([
                "raster subtract",
                "-p", "EPSG:4326",
                "-k", otherFile.absolutePath,
                "-j", "EPSG:4326"
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(result)), new Projection("EPSG:4326"))

        assertEquals 4, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 4, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 2, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }
}
