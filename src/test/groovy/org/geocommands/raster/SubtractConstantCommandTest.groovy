package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.SubtractConstantCommand.SubtractConstantOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The SubtractConstantCommand Unit Test
 * @author Jared Erickson
 */
class SubtractConstantCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_subtract_constant", ".tif")
        SubtractConstantCommand command = new SubtractConstantCommand()
        SubtractConstantOptions options = new SubtractConstantOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                values: [10]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
        pt = new Point(120, 45)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void executeFromWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_subtract_constant", ".tif")
        SubtractConstantCommand command = new SubtractConstantCommand()
        SubtractConstantOptions options = new SubtractConstantOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                values: [255],
                from: true
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(255 - inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
        pt = new Point(120, 45)
        assertEquals(255 - inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        SubtractConstantCommand command = new SubtractConstantCommand()
        SubtractConstantOptions options = new SubtractConstantOptions(
                values: [10]
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(writer.toString())))
        assertNotNull(outRaster)
        Raster inRaster = format.read(getStringReader("raster.asc").text)

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_subtract_constant", ".tif")
        runApp([
                "raster subtract constant",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-v", "10"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
        pt = new Point(120, 45)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void runAsCommandLineWithFilesAndFrom() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_subtract_constant", ".tif")
        runApp([
                "raster subtract constant",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-v", "255",
                "-m", "true"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(255 - inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
        pt = new Point(120, 45)
        assertEquals(255 - inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String boundStr = "100 100 200 200"
        String result = runApp([
                "raster subtract constant",
                "-v", "10"
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(result)))
        assertNotNull(outRaster)
        Raster inRaster = format.read(getStringReader("raster.asc").text)

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt, 0) - 10, outRaster.getValue(pt, 0), 0.1)
    }
}
