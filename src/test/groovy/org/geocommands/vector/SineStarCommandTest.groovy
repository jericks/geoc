package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.SineStarCommand.SineStarOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The SineStarCommand Unit Test
 * @author Jared Erickson
 */
class SineStarCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        SineStarCommand cmd = new SineStarCommand()
        SineStarOptions options = new SineStarOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath,
                numPoints: 200,
                rotation: 45,
                numberOfArms: 6,
                armLengthRatio: 0.85
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        SineStarCommand cmd = new SineStarCommand()
        SineStarOptions options = new SineStarOptions(
                geometry: "centroid(geom)",
                width: 5280 * 2,
                height: 5280,
                numPoints: 100,
                rotation: 0,
                numberOfArms: 10,
                armLengthRatio: 0.75
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
                "vector sinestar",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", 50,
                "-a", 90,
                "-n", 5,
                "-e", 0.85
        ], "")
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void runWithText() {
        String result = runApp([
                "vector sinestar",
                "-g", "centroid(geom)",
                "-w", 5280,
                "-h", 5280 * 2,
                "-p", 160,
                "-n", 15,
                "-e", 0.55
        ], getStringReader("polys.csv").text)
        Layer layer = new CsvReader().read(result)
        assertEquals(3, layer.count)
    }
}

