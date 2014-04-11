package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.InvertCommand.InvertOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The InvertCommand Unit Test
 * @author Jared Erickson
 */
class InvertCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_invert", ".tif")
        InvertCommand command = new InvertCommand()
        InvertOptions options = new InvertOptions(
                inputRaster: inFile,
                outputRaster: outFile
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
        InvertCommand command = new InvertCommand()
        InvertOptions options = new InvertOptions()
        command.execute(options, reader, writer)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0, 84.6)
        assertEquals(-inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
        pt = new Point(-174.2, 85.0)
        assertEquals(-inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_invert", ".tif")
        runApp([
                "raster invert",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
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
        String result = runApp([
                "raster invert"
        ], reader.text)

        ArcGrid outFormat = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = outFormat.read()
        assertNotNull(outRaster)
        ArcGrid inFormat = new ArcGrid(getStringReader("raster.asc").text)
        Raster inRaster = inFormat.read()

        Point pt = new Point(-175.0, 84.6)
        assertEquals(-inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
        pt = new Point(-174.2, 85.0)
        assertEquals(-inRaster.getValue(pt, 0), outRaster.getValue(pt, 0), 0.1)
    }
}
