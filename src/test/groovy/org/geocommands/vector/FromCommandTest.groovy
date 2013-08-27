package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.FromCommand.FromOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The FromCommand Unit Test
 * @author Jared Erickson
 */
class FromCommandTest extends BaseTest {

    @Test void execute() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
            text: file.text,
            format: "kml",
            geometryType: "Point",
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals "Point", shp.schema.geom.typ
        assertEquals 3, shp.count
        shp.eachFeature {f->
            assertNotNull f.geom
        }
    }

    @Test void executeFromKmlToCsv() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
            format: "kml",
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(file.text), writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals "Point", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature {f->
            assertNotNull f.geom
        }
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        App.main([
            "vector from",
            "-t", file.text,
            "-f", "kml",
            "-g", "Point",
            "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals "Point", shp.schema.geom.typ
        assertEquals 3, shp.count
        shp.eachFeature {f->
            assertNotNull f.geom
        }

        String output = runApp(["vector from", "-f", "kml"], file.text)
        Layer layer = getLayerFromCsv(output)
        assertEquals "Point", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature {f->
            assertNotNull f.geom
        }
    }
}
