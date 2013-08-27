package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.UpdateCommand.UpdateOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The UpdateCommand Unit Test
 * @author Jared Erickson
 */
class UpdateCommandTest extends BaseTest {

    @Test void execute() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_update")

        UpdateCommand cmd = new UpdateCommand()
        UpdateOptions options = new UpdateOptions(
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
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals 2, layer.count("A IS NULL")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 95 105, 95 95, 85 95)))", layer.getFeatures("A IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 105, 125 105, 125 95, 97 95)))", layer.getFeatures("A IS NULL")[1].geom.wkt
    }

    @Test void executeWithCsv() {
        UpdateCommand cmd = new UpdateCommand()
        UpdateOptions options = new UpdateOptions(
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
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals 2, layer.count("A = ''")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 95 105, 95 95, 85 95)))", layer.getFeatures("A = ''")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 105, 125 105, 125 95, 97 95)))", layer.getFeatures("A = ''")[1].geom.wkt
    }

    @Test void runAsCommandLine() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_update")
        App.main([
                "vector update",
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
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals 2, layer.count("A IS NULL")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 95 105, 95 95, 85 95)))", layer.getFeatures("A IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 105, 125 105, 125 95, 97 95)))", layer.getFeatures("A IS NULL")[1].geom.wkt

        String output = runApp(["vector update","-k", bFile.absolutePath],readCsv("a.csv").text)
        layer = getLayerFromCsv(output)
        // Check schema
        assertTrue layer.schema.has("A")
        assertFalse layer.schema.has("B")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A = 1")
        assertEquals 1, layer.count("A = 2")
        assertEquals 2, layer.count("A = ''")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A = 1")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A = 2")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 95 105, 95 95, 85 95)))", layer.getFeatures("A = ''")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 105, 125 105, 125 95, 97 95)))", layer.getFeatures("A = ''")[1].geom.wkt
    }

}
