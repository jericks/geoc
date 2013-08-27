package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * The ClipCommand Unit Test
 * @author Jared Erickson
 */
class ClipCommandTest extends BaseTest {

    @Test void execute() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_clip")

        ClipCommand cmd = new ClipCommand()
        ClipCommand.ClipOptions options = new ClipCommand.ClipOptions(
            inputWorkspace: aFile.absolutePath,
            otherWorkspace: bFile.absolutePath,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)

        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 3, layer.count
        assertEquals 2, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A = 1")[1].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

    @Test void executeWithCsv() {
        ClipCommand cmd = new ClipCommand()
        ClipCommand.ClipOptions options = new ClipCommand.ClipOptions(
            otherWorkspace: getResource("b.properties").absolutePath
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("a.csv"), w)
        println w.toString()
        Layer layer = getLayerFromCsv(w.toString())
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 3, layer.count
        assertEquals 2, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A = 1")[1].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

    @Test void runAsCommandLine() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_clip")
        App.main([
                "vector clip",
                "-i", aFile.absolutePath,
                "-k", bFile.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 3, layer.count
        assertEquals 2, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A = 1")[1].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A = 2")[0].geom.wkt

        String output = runApp(["vector clip","-k", bFile.absolutePath],readCsv("a.csv").text)
        layer = getLayerFromCsv(output)
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 3, layer.count
        assertEquals 2, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A = 1")[1].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

}
