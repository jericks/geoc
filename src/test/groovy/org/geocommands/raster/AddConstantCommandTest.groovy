package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.raster.AddConstantCommand.AddConstantOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The AddConstantCommand Unit Test
 * @author Jared Erickson
 */
class AddConstantCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_add_constant", ".tif")
        AddConstantCommand command = new AddConstantCommand()
        AddConstantOptions options = new AddConstantOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                values: [10]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120,47)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
        pt = new Point(120,45)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        AddConstantCommand command = new AddConstantCommand()
        AddConstantOptions options = new AddConstantOptions(
                values: [10]
        )
        command.execute(options, reader, writer)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_add_constant", ".tif")
        runApp([
                "raster add constant",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-v", "10"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120,47)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
        pt = new Point(120,45)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster add constant",
                "-v", "10"
        ], reader.text)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0,84.6)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
        pt = new Point(-174.2,85.0)
        assertEquals(inRaster.getValue(pt,0) + 10, outRaster.getValue(pt,0), 0.1)
    }
}
