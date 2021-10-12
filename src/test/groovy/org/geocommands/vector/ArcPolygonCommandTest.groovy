package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.ArcPolygonCommand.ArcPolygonOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The ArcPolygonCommand Unit Test
 * @author Jared Erickson
 */
class ArcPolygonCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        ArcPolygonCommand cmd = new ArcPolygonCommand()
        ArcPolygonOptions options = new ArcPolygonOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath,
                startAngle: 0,
                endAngle: 45
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        ArcPolygonCommand cmd = new ArcPolygonCommand()
        ArcPolygonOptions options = new ArcPolygonOptions(
                geometry: "centroid(geom)",
                width: 5280 * 2,
                height: 5280,
                numPoints: 8,
                startAngle: 230,
                endAngle: 275
        )
        cmd.execute(options, reader, writer)
        Layer layer = new CsvReader().read(writer.toString())
        assertEquals(3, layer.count)
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        runApp([
                "vector arcpolygon",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", 40,
                "-s", 90,
                "-e", 180
        ], "")
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void runWithText() {
        String result = runApp([
                "vector arcpolygon",
                "-g", "centroid(geom)",
                "-w", 5280,
                "-h", 5280 * 2,
                "-p", 16,
                "-s", 45,
                "-e", 275
        ], getStringReader("polys.csv").text)
        Layer layer = new CsvReader().read(result)
        assertEquals(3, layer.count)
    }
}

