package org.geocommands.raster

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.raster.PointCommand.PointOptions
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * The PointCommand Unit Test
 * @author Jared Erickson
 */
class PointCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.asc")
        File outFile = createTemporaryFile("points", "shp")
        PointCommand command = new PointCommand()
        PointOptions options = new PointOptions(
                inputRaster: inFile,
                outputWorkspace: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value0"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("point"))
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        PointCommand command = new PointCommand()
        PointOptions options = new PointOptions()
        command.execute(options, reader, writer)

        Layer layer = new CsvReader().read(writer.toString())
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value0"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("point"))
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.asc")
        File outFile = createTemporaryFile("points", "shp")
        runApp([
                "raster point",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value0"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("point"))
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster point"
        ], reader.text)

        Layer layer = new CsvReader().read(result)
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value0"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("point"))
    }
}
