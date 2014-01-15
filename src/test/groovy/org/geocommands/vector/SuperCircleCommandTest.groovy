package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.SuperCircleCommand.SuperCircleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The SuperCircleCommand Unit Test
 * @author Jared Erickson
 */
class SuperCircleCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        SuperCircleCommand cmd = new SuperCircleCommand()
        SuperCircleOptions options = new SuperCircleOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath,
                power: 0.5
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        SuperCircleCommand cmd = new SuperCircleCommand()
        SuperCircleOptions options = new SuperCircleOptions(
                geometry: "centroid(geom)",
                width: 5280 * 2,
                height: 5280,
                numPoints: 8,
                power: 0.75
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
                "vector supercircle",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", 40,
                "-e", 0.4
        ], "")
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void runWithText() {
        String result = runApp([
                "vector supercircle",
                "-g", "centroid(geom)",
                "-w", 5280,
                "-h", 5280 * 2,
                "-p", 16,
                "-e", 0.25,
        ], getStringReader("polys.csv").text)
        Layer layer = new CsvReader().read(result)
        assertEquals(3, layer.count)
    }
}

