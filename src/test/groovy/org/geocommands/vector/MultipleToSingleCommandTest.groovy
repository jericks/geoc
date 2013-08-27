package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.MultipleToSingleCommand.MultipleToSingleOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The MultipleToSingleCommand Unit Test
 * @author Jared Erickson
 */
class MultipleToSingleCommandTest extends BaseTest {

    @Test void execute() {
        MultipleToSingleCommand cmd = new MultipleToSingleCommand()
        File file = getResource("multi.properties")
        File shpFile = createTemporaryShapefile("single")
        MultipleToSingleOptions options = new MultipleToSingleOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "Point", shp.schema.geom.typ
    }

    @Test void executeWithCsv() {
        MultipleToSingleCommand cmd = new MultipleToSingleCommand()
        MultipleToSingleOptions options = new MultipleToSingleOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("multi.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        assertEquals "Point", layer.schema.geom.typ
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("single")
        String output = runApp([
            "vector multiple2single",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath
        ],"")
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "Point", shp.schema.geom.typ

        output = runApp(["vector multiple2single"], readCsv("multi.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        assertEquals "Point", layer.schema.geom.typ
    }
}