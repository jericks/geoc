package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.CropCommand.CropOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The CropCommand Unit Test
 * @author Jared Erickson
 */
class CropCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        CropCommand command = new CropCommand()
        CropOptions options = new CropOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                bounds: "1166191.0260847565, 822960.0090852415, 1166761.4391797914, 823593.1955759579"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Bounds.fromString(options.bounds)
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        CropCommand command = new CropCommand()
        CropOptions options = new CropOptions(
                bounds: "-176.0 81.6 -174.1 82.4"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Bounds.fromString(options.bounds)
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        String boundStr = "1166191.0260847565, 822960.0090852415, 1166761.4391797914, 823593.1955759579"
        runApp([
                "raster crop",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-b", boundStr
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Bounds.fromString(boundStr)
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String boundStr = "-176.0 81.6 -174.1 82.4"
        String result = runApp([
                "raster crop",
                "-b", boundStr
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Bounds.fromString(boundStr)
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }
}
