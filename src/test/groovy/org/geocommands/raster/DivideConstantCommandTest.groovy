package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.DivideConstantCommand.DivideConstantOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The DivideConstantCommand Unit Test
 * @author Jared Erickson
 */
class DivideConstantCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_divide_constant", ".tif")
        DivideConstantCommand command = new DivideConstantCommand()
        DivideConstantOptions options = new DivideConstantOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                values: [2]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(120, 45)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        DivideConstantCommand command = new DivideConstantCommand()
        DivideConstantOptions options = new DivideConstantOptions(
                values: [2]
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(writer.toString())))
        assertNotNull(outRaster)
        Raster inRaster = format.read(getStringReader("raster.asc").text)

        Point pt = new Point(-175.0, 84.6)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(-174.2, 85.0)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_divide_constant", ".tif")
        runApp([
                "raster divide constant",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-v", "2"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        Raster inRaster = format.read(inFile)

        Point pt = new Point(-120, 47)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(120, 45)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster divide constant",
                "-v", "2"
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(result)))
        assertNotNull(outRaster)
        Raster inRaster = format.read(getStringReader("raster.asc").text)

        Point pt = new Point(-175.0, 84.6)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(-174.2, 85.0)
        assertEquals(inRaster.getValue(pt, 0) / 2, outRaster.getValue(pt, 0), 1.0)
    }
}
