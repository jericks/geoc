package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddFieldsCommand.AddFieldsOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The AddFieldsCommand UnitTest
 * @author Jared Erickson
 */
class AddFieldsCommandTest extends BaseTest {

    @Test
    void execute() {
        AddFieldsCommand cmd = new AddFieldsCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddFieldsOptions options = new AddFieldsCommand.AddFieldsOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                fields: [
                        area  : "double",
                        length: "double"
                ]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("area")
        assertTrue shp.schema.has("length")
    }

    @Test
    void executeWithCsv() {
        AddFieldsCommand cmd = new AddFieldsCommand()
        AddFieldsOptions options = new AddFieldsOptions(fields: [
                area  : "double",
                length: "double"
        ])
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertTrue layer.schema.has("area")
        assertTrue layer.schema.has("length")
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector addfields",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "area=double",
                "-f", "length=double"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("area")
        assertTrue shp.schema.has("length")

        String output = runApp(["vector addfields", "-f", "area=double", "-f", "length=double"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertTrue layer.schema.has("area")
        assertTrue layer.schema.has("length")
    }
}
