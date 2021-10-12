package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.EnvelopesCommand.EnvelopesOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The EnvelopesCommand Unit Test
 * @author Jared Erickson
 */
class EnvelopesCommandTest extends BaseTest {

    @Test
    void execute() {
        EnvelopesCommand cmd = new EnvelopesCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("envelopes")
        EnvelopesOptions options = new EnvelopesOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        shp.eachFeature { f ->
            assertEquals 5, f.geom.numPoints
        }
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        EnvelopesCommand cmd = new EnvelopesCommand()
        EnvelopesOptions options = new EnvelopesOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        layer.eachFeature { f ->
            assertEquals 5, f.geom.numPoints
        }
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("envelopes")
        App.main([
                "vector envelopes",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        shp.eachFeature { f ->
            assertEquals 5, f.geom.numPoints
        }
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector envelopes"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        layer.eachFeature { f ->
            assertEquals 5, f.geom.numPoints
        }
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
