package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.DivideRastersCommand.DivideRastersOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The DivideRastersCommand Unit Test
 * @author Jared Erickson
 */
class DivideRastersCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_add", "tif")
        DivideRastersCommand command = new DivideRastersCommand()
        DivideRastersOptions options = new DivideRastersOptions(
                inputRaster: inFile,
                inputProjection: "EPSG:4326",
                otherRaster: otherFile,
                otherProjection: "EPSG:4326",
                outputRaster: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 3, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 2.33, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 1.5, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster1.acs")
        File otherFile = getResource("raster2.acs")
        StringWriter writer = new StringWriter()
        DivideRastersCommand command = new DivideRastersCommand()
        DivideRastersOptions options = new DivideRastersOptions(
                inputProjection: "EPSG:4326",
                otherRaster: otherFile,
                otherProjection: "EPSG:4326"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = format.read(new Projection("EPSG:4326"))

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 3, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 2.33, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 1.5, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster1.acs")
        File otherFile = getResource("raster2.acs")
        File outFile = createTemporaryFile("raster_add", "tif")
        runApp([
                "raster divide",
                "-i", inFile.absolutePath,
                "-p", "EPSG:4326",
                "-k", otherFile.absolutePath,
                "-j", "EPSG:4326",
                "-o", outFile.absolutePath
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 3, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 2.33, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 1.5, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        File otherFile = getResource("raster2.acs")
        String result = runApp([
                "raster divide",
                "-p", "EPSG:4326",
                "-k", otherFile.absolutePath,
                "-j", "EPSG:4326"
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = format.read(new Projection("EPSG:4326"))

        assertEquals 5, outRaster.eval(new Point(0.5, 0.5))[0], 0.1
        assertEquals 3, outRaster.eval(new Point(1.5, 1.5))[0], 0.1
        assertEquals 2.33, outRaster.eval(new Point(2.5, 2.5))[0], 0.1
        assertEquals 1.5, outRaster.eval(new Point(3.5, 2.5))[0], 0.1
    }
}
