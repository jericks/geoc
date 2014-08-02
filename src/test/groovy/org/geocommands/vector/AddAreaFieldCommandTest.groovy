package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddAreaFieldCommand.AddAreaFieldOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The AddAreaFieldCommand UniTest
 * @author Jared Erickson
 */
class AddAreaFieldCommandTest extends BaseTest {

    @Test
    void execute() {
        AddAreaFieldCommand cmd = new AddAreaFieldCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddAreaFieldOptions options = new AddAreaFieldOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("AREA")
        shp.eachFeature { f ->
            assertTrue f["AREA"] > 0
        }
    }

    @Test
    void executeWithCsv() {
        AddAreaFieldCommand cmd = new AddAreaFieldCommand()
        AddAreaFieldOptions options = new AddAreaFieldOptions(areaFieldName: "THE_AREA")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertTrue layer.schema.has("THE_AREA")
        layer.eachFeature { f ->
            assertTrue((f["THE_AREA"] as Double) > 0)
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector addareafield",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "AREA2"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("AREA2")
        shp.eachFeature { f ->
            assertTrue f["AREA2"] > 0
        }

        String output = runApp(["vector addareafield", "-f", "AREA3"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertTrue layer.schema.has("AREA3")
        layer.eachFeature { f ->
            assertTrue((f["AREA3"] as Double) > 0)
        }
    }
}
