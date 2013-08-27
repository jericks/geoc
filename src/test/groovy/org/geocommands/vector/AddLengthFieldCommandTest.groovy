package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.vector.AddLengthFieldCommand.AddLengthFieldOptions
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test

/**
 * The AddLengthFieldCommand UniTest
 * @author Jared Erickson
 */
class AddLengthFieldCommandTest extends BaseTest {

    @Test void execute() {
        AddLengthFieldCommand cmd = new AddLengthFieldCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        AddLengthFieldOptions options = new AddLengthFieldOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 4, shp.count
        org.junit.Assert.assertTrue shp.schema.has("LENGTH")
        shp.eachFeature {f->
            org.junit.Assert.assertTrue f["LENGTH"] > 0
        }
    }

    @Test void executeWithCsv() {
        AddLengthFieldCommand cmd = new AddLengthFieldCommand()
        AddLengthFieldOptions options = new AddLengthFieldOptions(lengthFieldName: "THE_LENGTH")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        org.junit.Assert.assertEquals 4, layer.count
        org.junit.Assert.assertTrue layer.schema.has("THE_LENGTH")
        layer.eachFeature {f->
            org.junit.Assert.assertTrue((f["THE_LENGTH"] as Double) > 0)
        }
    }

    @Test void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
            "vector addlengthfield",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath,
            "-f", "PERIMETER"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 4, shp.count
        org.junit.Assert.assertTrue shp.schema.has("PERIMETER")
        shp.eachFeature {f->
            org.junit.Assert.assertTrue f["PERIMETER"] > 0
        }

        String output = runApp(["vector addlengthfield","-f","PERIM"],readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        org.junit.Assert.assertEquals 4, layer.count
        org.junit.Assert.assertTrue layer.schema.has("PERIM")
        layer.eachFeature {f->
            org.junit.Assert.assertTrue((f["PERIM"] as Double) > 0)
        }
    }
}
