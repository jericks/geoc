package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.EraseCommand.EraseOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The EraseCommand Unit Test
 * @author Jared Erickson
 */
class EraseCommandTest extends BaseTest {

    @Test
    void execute() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_erase")

        EraseCommand cmd = new EraseCommand()
        EraseOptions options = new EraseOptions(
                inputWorkspace: aFile.absolutePath,
                otherWorkspace: bFile.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)

        // Check schema
        assertTrue layer.name.contains("a_b_erase")
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 2, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

    @Test
    void executeWithCsv() {
        EraseCommand cmd = new EraseCommand()
        EraseOptions options = new EraseOptions(
                otherWorkspace: getResource("b.properties").absolutePath
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("a.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 2, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

    @Test
    void runAsCommandLine() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_erase")
        App.main([
                "vector erase",
                "-i", aFile.absolutePath,
                "-k", bFile.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        // Check schema
        assertTrue layer.name.contains("a_b_erase")
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 2, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt

        String output = runApp(["vector erase", "-k", bFile.absolutePath], readCsv("a.csv").text)
        layer = getLayerFromCsv(output)
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 2, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
    }

}
