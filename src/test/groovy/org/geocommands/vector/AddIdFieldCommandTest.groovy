package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddIdFieldCommand.AddIdFieldOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

/**
 * The AddIdFieldCommand UniTest
 * @author Jared Erickson
 */
class AddIdFieldCommandTest extends BaseTest {

    @Test
    void execute() {
        AddIdFieldCommand cmd = new AddIdFieldCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddIdFieldOptions options = new AddIdFieldOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                idFieldName: "THE_ID"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("THE_ID")
        assertEquals 1, shp.getFeatures("THE_ID=1")[0]["THE_ID"]
        assertEquals 2, shp.getFeatures("THE_ID=2")[0]["THE_ID"]
        assertEquals 3, shp.getFeatures("THE_ID=3")[0]["THE_ID"]
        assertEquals 4, shp.getFeatures("THE_ID=4")[0]["THE_ID"]
    }

    @Test
    void executeWithCsv() {
        AddIdFieldCommand cmd = new AddIdFieldCommand()
        AddIdFieldOptions options = new AddIdFieldOptions(idFieldName: "THE_ID")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertTrue layer.schema.has("THE_ID")
        assertEquals 1, layer.getFeatures("THE_ID=1")[0]["THE_ID"]
        assertEquals 2, layer.getFeatures("THE_ID=2")[0]["THE_ID"]
        assertEquals 3, layer.getFeatures("THE_ID=3")[0]["THE_ID"]
        assertEquals 4, layer.getFeatures("THE_ID=4")[0]["THE_ID"]
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector addidfield",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "THE_ID",
                "-s", 100
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("THE_ID")
        assertEquals 100, shp.getFeatures("THE_ID=100")[0]["THE_ID"]
        assertEquals 101, shp.getFeatures("THE_ID=101")[0]["THE_ID"]
        assertEquals 102, shp.getFeatures("THE_ID=102")[0]["THE_ID"]
        assertEquals 103, shp.getFeatures("THE_ID=103")[0]["THE_ID"]

        String output = runApp(["vector addidfield", "-f", "THE_ID"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertTrue layer.schema.has("THE_ID")
        assertEquals 1, layer.getFeatures("THE_ID=1")[0]["THE_ID"]
        assertEquals 2, layer.getFeatures("THE_ID=2")[0]["THE_ID"]
        assertEquals 3, layer.getFeatures("THE_ID=3")[0]["THE_ID"]
        assertEquals 4, layer.getFeatures("THE_ID=4")[0]["THE_ID"]
    }
}
