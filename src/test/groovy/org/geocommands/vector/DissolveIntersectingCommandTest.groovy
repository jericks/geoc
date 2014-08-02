package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DissolveIntersectingCommand.DissolveIntersectingOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The DissolveIntersectingCommand Unit Test
 * @author Jared Erickson
 */
class DissolveIntersectingCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("dissolve")
        DissolveIntersectingCommand cmd = new DissolveIntersectingCommand()
        DissolveIntersectingOptions options = new DissolveIntersectingOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertTrue shp.schema.has("id")
        assertTrue shp.schema.has("count")
        assertEquals 3, shp.count("count = 2")
        assertEquals 3, shp.count("count = 1")
    }

    @Test
    void executeCsv() {
        DissolveIntersectingCommand cmd = new DissolveIntersectingCommand()
        DissolveIntersectingOptions options = new DissolveIntersectingOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("overlapping.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertTrue layer.schema.has("id")
        assertTrue layer.schema.has("count")
        assertEquals 3, layer.count("count = 2")
        assertEquals 3, layer.count("count = 1")
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("dissolve")
        App.main([
                "vector dissolveintersecting",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertTrue shp.schema.has("id")
        assertTrue shp.schema.has("count")
        assertEquals 3, shp.count("count = 2")
        assertEquals 3, shp.count("count = 1")

        String output = runApp(["vector dissolveintersecting"], readCsv("overlapping.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertTrue layer.schema.has("id")
        assertTrue layer.schema.has("count")
        assertEquals 3, layer.count("count = 2")
        assertEquals 3, layer.count("count = 1")
    }

}
