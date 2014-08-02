package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddXYFieldsCommand.AddXYFieldsOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The AddXYFieldsCommand UniTest
 * @author Jared Erickson
 */
class AddXYFieldsCommandTest extends BaseTest {

    @Test
    void execute() {
        AddXYFieldsCommand cmd = new AddXYFieldsCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddXYFieldsOptions options = new AddXYFieldsOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("X")
        assertTrue shp.schema.has("Y")
        shp.eachFeature { f ->
            assertTrue f["X"] > 0
            assertTrue f["Y"] > 0
        }
    }

    @Test
    void executeWithCsv() {
        AddXYFieldsCommand cmd = new AddXYFieldsCommand()
        AddXYFieldsOptions options = new AddXYFieldsOptions(xFieldName: "X_COORD", yFieldName: "Y_COORD")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertTrue layer.schema.has("X_COORD")
        assertTrue layer.schema.has("Y_COORD")
        layer.eachFeature { f ->
            assertTrue((f["X_COORD"] as Double) > 0)
            assertTrue((f["Y_COORD"] as Double) > 0)
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector addxyfields",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-x", "X_COORD",
                "-y", "Y_COORD"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("X_COORD")
        assertTrue shp.schema.has("Y_COORD")
        shp.eachFeature { f ->
            assertTrue f["X_COORD"] > 0
            assertTrue f["Y_COORD"] > 0
        }

        String output = runApp(["vector addxyfields", "-x", "X_COORD", "-y", "Y_COORD"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertTrue shp.schema.has("X_COORD")
        assertTrue shp.schema.has("Y_COORD")
        shp.eachFeature { f ->
            assertTrue f["X_COORD"] > 0
            assertTrue f["Y_COORD"] > 0
        }
    }
}
