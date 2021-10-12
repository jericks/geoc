package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.vector.AttributeJoinCommand.AttributeOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The TabularJoinCommand Unit Test
 * @author Jared Erickson
 */
class AttributeJoinCommandTest extends BaseTest {

    @Test void executeWithCsv() {
        AttributeJoinCommand cmd = new AttributeJoinCommand()
        File outFile = createTemporaryShapefile("joined")
        AttributeOptions options = new AttributeOptions(
                inputWorkspace: getResource("polygons.properties").absolutePath,
                outputWorkspace: outFile.absolutePath,
                joinSource: getResource("join.csv").absolutePath,
                joinFieldName: "key",
                layerFieldName: "id"
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals 4, layer.count
        ["id","the_geom","row","col","key","name","descriptio"].each { String name ->
            assertTrue layer.schema.has(name)
        }
        layer.eachFeature {Feature f ->
            ["id","the_geom","row","col","key","name","descriptio"].each { String name ->
                if (f["id"] == 3 && name in ["key", "name", "descriptio"]) {
                    assertNull f[name]
                } else {
                    assertNotNull f[name]
                }
            }
        }
    }

    @Test void executeWithDbf() {
        AttributeJoinCommand cmd = new AttributeJoinCommand()
        File outFile = createTemporaryShapefile("joined")
        AttributeOptions options = new AttributeOptions(
                inputWorkspace: getResource("polygons.properties").absolutePath,
                outputWorkspace: outFile.absolutePath,
                joinSource: getResource("join.dbf").absolutePath,
                joinFieldName: "KEY",
                layerFieldName: "id"
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals 4, layer.count
        ["id","the_geom","row","col","KEY","NAME","DESCRIPTIO"].each { String name ->
            assertTrue layer.schema.has(name)
        }
        layer.eachFeature {Feature f ->
            ["id","the_geom","row","col","KEY","NAME","DESCRIPTIO"].each { String name ->
                if (f["id"] == 3 && name in ["KEY","NAME","DESCRIPTIO"]) {
                    assertNull f[name]
                } else {
                    assertNotNull f[name]
                }
            }
        }
    }

    @Test void runWithCsv() {
        String result = runApp([
                "vector join attribute",
                "-s", getResource("join.csv").absolutePath,
                "-j", "key",
                "-y", "id",
                "-n", "name",
                "-n", "description"
        ], getStringReader("polygons.csv").text)
        Layer layer = getLayerFromCsv(result)
        assertEquals 4, layer.count
        assertFalse layer.schema.has("key")
        ["id","the_geom","row","col","name","description"].each { String name ->
            assertTrue layer.schema.has(name)
        }
        layer.eachFeature {Feature f ->
            ["id","the_geom","row","col","name","description"].each { String name ->
                if (f["id"] == 3 && name in ["name", "description"]) {
                    assertNull f[name]
                } else {
                    assertNotNull f[name]
                }
            }
        }
    }

    @Test void runWithDbf() {
        String result = runApp([
                "vector join attribute",
                "-s", getResource("join.dbf").absolutePath,
                "-j", "KEY",
                "-y", "id",
                "-n", "NAME",
                "-n", "DESCRIPTIO",
                "-m"
        ], getStringReader("polygons.csv").text)
        Layer layer = getLayerFromCsv(result)
        assertEquals 3, layer.count
        assertFalse layer.schema.has("key")
        ["id","the_geom","row","col","NAME","DESCRIPTIO"].each { String name ->
            assertTrue layer.schema.has(name)
        }
        layer.eachFeature {Feature f ->
            ["id","the_geom","row","col","NAME","DESCRIPTIO"].each { String name ->
                if (f["id"] == 3 && name in ["NAME","DESCRIPTIO"]) {
                    assertNull f[name]
                } else {
                    assertNotNull f[name]
                }
            }
        }
    }
}
