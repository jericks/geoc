package org.geocommands.raster

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.raster.PolygonCommand.PolygonOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The PolygonCommand Unit Test
 * @author Jared Erickson
 */
class PolygonCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.asc")
        File outFile = createTemporaryFile("polygons", "shp")
        PolygonCommand command = new PolygonCommand()
        PolygonOptions options = new PolygonOptions(
                inputRaster: inFile,
                outputWorkspace: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multipolygon"))
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        PolygonCommand command = new PolygonCommand()
        PolygonOptions options = new PolygonOptions()
        command.execute(options, reader, writer)

        Layer layer = new CsvReader().read(writer.toString())
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("polygon"))
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.asc")
        File outFile = createTemporaryFile("polygons", "shp")
        runApp([
                "raster polygon",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multipolygon"))
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster polygon"
        ], reader.text)

        Layer layer = new CsvReader().read(result)
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("polygon"))
    }
}
