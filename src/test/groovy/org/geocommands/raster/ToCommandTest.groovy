package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.layer.WorldImage
import org.geocommands.BaseTest
import org.geocommands.raster.ToCommand.ToOptions
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * The ToCommand Unit Test
 */
class ToCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster", "png")
        ToCommand command = new ToCommand()
        ToOptions options = new ToOptions(
            inputRaster: inFile,
            outputRaster: outFile,
            outputFormat: "worldimage"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Format outFormat = Format.getFormat(outFile)
        assertTrue(outFormat instanceof WorldImage)
        Raster outRaster = outFormat.read(outFile)
        assertNotNull(outRaster)
    }

    // @TODO This doesn't work when the output file is PNG
    @Test
    void executeStringToFile() {
        StringReader reader = getStringReader("raster.asc")
        File outFile = createTemporaryFile("raster", "tif")
        ToCommand command = new ToCommand()
        ToOptions options = new ToOptions(
            inputProjection: "EPSG:4326",
            outputRaster: outFile
        )
        command.execute(options, reader, new StringWriter())

        Format outFormat = Format.getFormat(outFile)
        assertTrue(outFormat instanceof GeoTIFF)
        Raster outRaster = outFormat.read(outFile)
        assertNotNull(outRaster)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster", "png")
        runApp([
                "raster to",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-f", "worldimage"
        ], "")

        Format outFormat = Format.getFormat(outFile)
        assertTrue(outFormat instanceof WorldImage)
        Raster outRaster = outFormat.read(outFile)
        assertNotNull(outRaster)
    }

    @Test
    void runAsCommandLineStringToFile() {
        StringReader reader = getStringReader("raster.asc")
        File outFile = createTemporaryFile("raster", "tif")
        runApp([
                "raster to",
                "-o", outFile.absolutePath,
                "-p", "EPSG:4326",
        ], reader.text)

        Format outFormat = Format.getFormat(outFile)
        assertTrue(outFormat instanceof GeoTIFF)
        Raster outRaster = outFormat.read(outFile)
        assertNotNull(outRaster)
    }
}
