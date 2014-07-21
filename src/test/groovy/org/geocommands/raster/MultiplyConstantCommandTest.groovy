package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.MultiplyConstantCommand.MultiplyConstantOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The MultiplyConstantCommand Unit Test
 * @author Jared Erickson
 */
class MultiplyConstantCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_multiply_constant", ".tif")
        MultiplyConstantCommand command = new MultiplyConstantCommand()
        MultiplyConstantOptions options = new MultiplyConstantOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                values: [1.01]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF outFormat = new GeoTIFF(outFile)
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        GeoTIFF inFormat = new GeoTIFF(inFile)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-120,47)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
        pt = new Point(120,45)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        MultiplyConstantCommand command = new MultiplyConstantCommand()
        MultiplyConstantOptions options = new MultiplyConstantOptions(
                values: [1.01]
        )
        command.execute(options, reader, writer)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_multiply_constant", ".tif")
        runApp([
                "raster multiply constant",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-v", "1.01"
        ], "")

        GeoTIFF outFormat = new GeoTIFF(outFile)
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        GeoTIFF inFormat = new GeoTIFF(inFile)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-120,47)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
        pt = new Point(120,45)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String boundStr = "100 100 200 200"
        String result = runApp([
                "raster multiply constant",
                "-v", "1.01"
        ], reader.text)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt,0) * 1.01, outRaster.getValue(pt,0), 1.0)
    }
}
