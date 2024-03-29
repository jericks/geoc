package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.SymDifferenceCommand.SymDifferenceOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The SymDifferenceCommand Unit Test
 * @author Jared Erickson
 */
class SymDifferenceCommandTest extends BaseTest {

    @Test
    void execute() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_symdifference")

        SymDifferenceCommand cmd = new SymDifferenceCommand()
        SymDifferenceOptions options = new SymDifferenceOptions(
                inputWorkspace: aFile.absolutePath,
                otherWorkspace: bFile.absolutePath,
                outputWorkspace: shpFile,
                postfixAll: true
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)

        // Check schema
        assertTrue layer.name.startsWith("a_b_symdifference")
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 IS NULL AND B2 = 3")
        assertEquals 1, layer.count("A1 IS NULL AND B2 = 4")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 90 105, 90 100, 95 100, 95 95, 85 95)))", layer.getFeatures("A1 IS NULL AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 100, 100 100, 100 105, 120 105, 120 100, 125 100, 125 95, 97 95)))", layer.getFeatures("A1 IS NULL AND B2 = 4")[0].geom.wkt
    }

    @Test
    void executeWithCsv() {
        SymDifferenceCommand cmd = new SymDifferenceCommand()
        SymDifferenceOptions options = new SymDifferenceOptions(
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
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = '' AND B2 = 3")
        assertEquals 1, layer.count("A1 = '' AND B2 = 4")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 90 105, 90 100, 95 100, 95 95, 85 95)))", layer.getFeatures("A1 = '' AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 100, 100 100, 100 105, 120 105, 120 100, 125 100, 125 95, 97 95)))", layer.getFeatures("A1 = '' AND B2 = 4")[0].geom.wkt
    }

    @Test
    void runAsCommandLine() {
        File aFile = getResource("a.properties")
        File bFile = getResource("b.properties")
        File shpFile = createTemporaryShapefile("a_b_symdifference")
        App.main([
                "vector symdifference",
                "-i", aFile.absolutePath,
                "-k", bFile.absolutePath,
                "-o", shpFile.absolutePath,
                "-p"
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        // Check schema
        assertTrue layer.name.startsWith("a_b_symdifference")
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 IS NULL AND B2 = 3")
        assertEquals 1, layer.count("A1 IS NULL AND B2 = 4")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 90 105, 90 100, 95 100, 95 95, 85 95)))", layer.getFeatures("A1 IS NULL AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 100, 100 100, 100 105, 120 105, 120 100, 125 100, 125 95, 97 95)))", layer.getFeatures("A1 IS NULL AND B2 = 4")[0].geom.wkt

        String output = runApp(["vector symdifference", "-k", bFile.absolutePath, "-p"], readCsv("a.csv").text)
        layer = getLayerFromCsv(output)
        // Check schema
        assertEquals "csv", layer.name
        assertTrue layer.schema.has("A1")
        assertTrue layer.schema.has("B2")
        assertEquals "MultiPolygon", layer.schema.geom.typ
        // Check features
        assertEquals 4, layer.count
        assertEquals 1, layer.count("A1 = 1 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = 2 AND B2 IS NULL")
        assertEquals 1, layer.count("A1 = '' AND B2 = 3")
        assertEquals 1, layer.count("A1 = '' AND B2 = 4")
        assertEquals "MULTIPOLYGON (((90 105, 90 110, 100 110, 100 105, 97 105, 97 100, 95 100, 95 105, 90 105)))", layer.getFeatures("A1 = 1 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((120 105, 120 110, 130 110, 130 100, 125 100, 125 105, 120 105)))", layer.getFeatures("A1 = 2 AND B2 IS NULL")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((85 95, 85 105, 90 105, 90 100, 95 100, 95 95, 85 95)))", layer.getFeatures("A1 = '' AND B2 = 3")[0].geom.wkt
        assertEquals "MULTIPOLYGON (((97 95, 97 100, 100 100, 100 105, 120 105, 120 100, 125 100, 125 95, 97 95)))", layer.getFeatures("A1 = '' AND B2 = 4")[0].geom.wkt
    }

}
