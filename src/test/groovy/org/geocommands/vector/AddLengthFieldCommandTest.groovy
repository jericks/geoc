package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddLengthFieldCommand.AddLengthFieldOptions
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The AddLengthFieldCommand UniTest
 * @author Jared Erickson
 */
class AddLengthFieldCommandTest extends BaseTest {

    @Test
    void execute() {
        AddLengthFieldCommand cmd = new AddLengthFieldCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddLengthFieldOptions options = new AddLengthFieldOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("LENGTH")
        shp.eachFeature { f ->
            assertTrue f["LENGTH"] > 0
        }
    }

    @Test
    void executeWithCsv() {
        AddLengthFieldCommand cmd = new AddLengthFieldCommand()
        AddLengthFieldOptions options = new AddLengthFieldOptions(lengthFieldName: "THE_LENGTH")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertTrue layer.schema.has("THE_LENGTH")
        layer.eachFeature { f ->
            assertTrue((f["THE_LENGTH"] as Double) > 0)
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector addlengthfield",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "PERIMETER"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertTrue shp.schema.has("PERIMETER")
        shp.eachFeature { f ->
            assertTrue f["PERIMETER"] > 0
        }

        String output = runApp(["vector addlengthfield", "-f", "PERIM"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertTrue layer.schema.has("PERIM")
        layer.eachFeature { f ->
            assertTrue((f["PERIM"] as Double) > 0)
        }
    }
}
