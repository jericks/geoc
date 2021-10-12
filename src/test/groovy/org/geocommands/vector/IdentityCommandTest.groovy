package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.IdentityCommand.IdentityOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The IdentityCommand Unit Test
 * @author Jared Erickson
 */
class IdentityCommandTest extends BaseTest {

    @Test
    void execute() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_identity")

        IdentityCommand cmd = new IdentityCommand()
        IdentityOptions options = new IdentityOptions(
                inputWorkspace: aFile.absolutePath,
                otherWorkspace: bFile.absolutePath,
                outputWorkspace: shpFile,
                postfixAll: true
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)

        // Check schema
        assertTrue layer.name.startsWith("a_b_identity")
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 5, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 = 3")
        assertEquals 1, layer.count("A1 = 1 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A1 = 1 AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A1 = 1 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A1 = 2 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
    }

    @Test
    void executeWithCsv() {
        IdentityCommand cmd = new IdentityCommand()
        IdentityOptions options = new IdentityOptions(
                otherWorkspace: getResource("b.properties").absolutePath,
                postfixAll: true
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("a.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        // Check schema
        assertEquals "csv", layer.name
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 5, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 = 3")
        assertEquals 1, layer.count("A1 = 1 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A1 = 1 AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A1 = 1 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A1 = 2 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
    }

    @Test
    void runAsCommandLine() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_identity")
        App.main([
                "vector identity",
                "-i", aFile.absolutePath,
                "-k", bFile.absolutePath,
                "-o", shpFile.absolutePath,
                "--postfix-all"
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        // Check schema
        assertTrue layer.name.startsWith("a_b_identity")
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 5, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 = 3")
        assertEquals 1, layer.count("A1 = 1 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A1 = 1 AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A1 = 1 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A1 = 2 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt

        String output = runApp(["vector identity", "-k", bFile.absolutePath, "--postfix-all"], readCsv("a.csv").text)
        layer = getLayerFromCsv(output)
        // Check schema
        assertEquals "csv", layer.name
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 5, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 = 3")
        assertEquals 1, layer.count("A1 = 1 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 = 4")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals "MULTIPOLYGON (((90 100, 90 105, 95 105, 95 100, 90 100)))", layer.getFeatures("A1 = 1 AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((100 105, 100 100, 97 100, 97 105, 100 105)))", layer.getFeatures("A1 = 1 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 100, 120 105, 125 105, 125 100, 120 100)))", layer.getFeatures("A1 = 2 AND B2 = 4")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
    }

}
