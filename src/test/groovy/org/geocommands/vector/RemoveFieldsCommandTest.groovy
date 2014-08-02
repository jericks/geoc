package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.RemoveFieldsCommand.RemoveFieldsOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

/**
 * The RemoveFieldsCommand UnitTest
 * @author Jared Erickson
 */
class RemoveFieldsCommandTest extends BaseTest {

    @Test
    void execute() {
        RemoveFieldsCommand cmd = new RemoveFieldsCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        RemoveFieldsOptions options = new RemoveFieldsOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                fields: [
                        "row", "col"
                ]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertFalse shp.schema.has("row")
        assertFalse shp.schema.has("col")
    }

    @Test
    void executeWithCsv() {
        RemoveFieldsCommand cmd = new RemoveFieldsCommand()
        RemoveFieldsOptions options = new RemoveFieldsOptions(fields: [
                "row", "col"
        ])
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertFalse layer.schema.has("row")
        assertFalse layer.schema.has("col")
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector removefields",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "row",
                "-f", "col"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertFalse shp.schema.has("row")
        assertFalse shp.schema.has("col")

        String output = runApp(["vector removefields", "-f", "row", "-f", "col"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertFalse layer.schema.has("row")
        assertFalse layer.schema.has("col")
    }
}
