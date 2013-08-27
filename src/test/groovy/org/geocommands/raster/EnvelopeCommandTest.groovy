package org.geocommands.raster

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.raster.EnvelopeCommand.EnvelopeOptions
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * The EnvelopeCommand Unit Test
 * @author Jared Erickson
 */
class EnvelopeCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("envelope", "shp")
        EnvelopeCommand command = new EnvelopeCommand()
        EnvelopeOptions options = new EnvelopeOptions(
                inputRaster: inFile,
                outputWorkspace: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count == 1)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multipolygon"))
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        EnvelopeCommand command = new EnvelopeCommand()
        EnvelopeOptions options = new EnvelopeOptions()
        command.execute(options, reader, writer)

        Layer layer = new CsvReader().read(writer.toString())
        assertTrue(layer.count == 1)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("polygon"))
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("contours", "shp")
        runApp([
                "raster envelope",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count == 1)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multipolygon"))
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster envelope"
        ], reader.text)

        Layer layer = new CsvReader().read(result)
        assertTrue(layer.count == 1)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("polygon"))
    }
}
