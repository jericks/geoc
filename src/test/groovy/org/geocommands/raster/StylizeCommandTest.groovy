package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.StylizeCommand.StylizeOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

/**
 * The StylizeCommand Unit Test
 * @author Jared Erickson
 */
class StylizeCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_stylized", "tif")
        println outFile
        StylizeCommand command = new StylizeCommand()
        StylizeOptions options = new StylizeOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                sld: getResource("raster_colormap.sld")
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        Point pt = new Point(-120, 47)
        assertEquals(226.0, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(120, 45)
        assertEquals(156.0, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        StylizeCommand command = new StylizeCommand()
        StylizeOptions options = new StylizeOptions(
                sld: getResource("raster_colormap.sld")
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        Point pt = new Point(-175.0, 84.6)
        assertEquals(165.0, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(-174.2, 85.0)
        assertEquals(165.0, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File sldFile = getResource("raster_colormap.sld")
        File outFile = createTemporaryFile("raster_divide_constant", ".tif")
        runApp([
                "raster stylize",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-s", sldFile.absolutePath
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        Point pt = new Point(-120, 47)
        assertEquals(226.0, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(120, 45)
        assertEquals(156.0, outRaster.getValue(pt, 0), 1.0)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        File sldFile = getResource("raster_colormap.sld")
        String result = runApp([
                "raster stylize",
                "-s", sldFile.absolutePath
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = format.read()
        assertNotNull(outRaster)

        Point pt = new Point(-175.0, 84.6)
        assertEquals(165.0, outRaster.getValue(pt, 0), 1.0)
        pt = new Point(-174.2, 85.0)
        assertEquals(165.0, outRaster.getValue(pt, 0), 1.0)
    }

}