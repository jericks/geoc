package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.GeometryReaderCommand.GeometryReaderOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

/**
 * The GeometryReaderCommand Unit Test
 * @author Jared Erickson
 */
class GeometryReaderCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points.wkt")
        File shpFile = createTemporaryShapefile("points")
        GeometryReaderCommand cmd = new GeometryReaderCommand()
        GeometryReaderOptions options = new GeometryReaderOptions(
                text: file.text,
                outputWorkspace: shpFile.absolutePath
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)
        assertEquals 10, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Point
        }
    }

    @Test
    void executeCsv() {
        File file = getResource("points.wkt")
        GeometryReaderCommand cmd = new GeometryReaderCommand()
        GeometryReaderOptions options = new GeometryReaderOptions()
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(file.text), writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals 10, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Point
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.wkt")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector geomr",
                "-t", file.text,
                "-o", shpFile.absolutePath
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        assertEquals 10, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Point
        }

        String output = runApp(["vector geomr"], file.text)
        layer = getLayerFromCsv(output)
        assertEquals 10, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Point
        }
    }
}
