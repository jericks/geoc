package org.geocommands.proj

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.proj.EnvelopeCommand.EnvelopeOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The EnvelopeCommand Unit Test
 * @author Jared Erickson
 */
class EnvelopeCommandTest extends BaseTest {

    @Test
    void executeToShapefile() {
        EnvelopeCommand cmd = new EnvelopeCommand()
        File file = createTemporaryShapefile("epsg2927")
        EnvelopeOptions options = new EnvelopeOptions(
                epsg: "EPSG:2927",
                outputWorkspace: file.absolutePath
        )
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 1, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ
        assertEquals "EPSG:2927", layer.schema.proj.id
    }

    @Test
    void executeToCsv() {
        EnvelopeCommand cmd = new EnvelopeCommand()
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        EnvelopeOptions options = new EnvelopeOptions(
                epsg: "EPSG:2927"
        )
        cmd.execute(options, reader, writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
        assertEquals "EPSG:2927", layer.schema.proj.id
    }

    @Test
    void runToShapefile() {
        File file = createTemporaryShapefile("epsg2927")
        runApp([
                "proj envelope",
                "-e", "EPSG:2927",
                "-o", file.absolutePath
        ], "")
        Layer layer = new Shapefile(file)
        assertEquals 1, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ
        assertEquals "EPSG:2927", layer.schema.proj.id
    }

    @Test
    void runToCsv() {
        String result = runApp([
                "proj envelope",
                "-e", "EPSG:2927",
                "-g"
        ], "")
        Layer layer = getLayerFromCsv(result)
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
        assertEquals "EPSG:4326", layer.schema.proj.id
    }
}
