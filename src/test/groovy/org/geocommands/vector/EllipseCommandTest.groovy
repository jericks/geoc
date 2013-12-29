package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.EllipseCommand.EllipseOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The EllipseCommand Unit Test
 * @author Jared Erickson
 */
class EllipseCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        EllipseCommand cmd = new EllipseCommand()
        EllipseOptions options = new EllipseOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath,
                numPoints: 100,
                rotation: 45
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        EllipseCommand cmd = new EllipseCommand()
        EllipseOptions options = new EllipseOptions(
                geometry: "centroid(geom)",
                width: 5280 * 2,
                height: 5280,
                numPoints: 8,
                rotation: 0
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
                "vector ellipse",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-p", 50,
                "-a", 90
        ], "")
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
    }

    @Test
    void runWithText() {
        String result = runApp([
                "vector ellipse",
                "-g", "centroid(geom)",
                "-w", 5280,
                "-h", 5280 * 2,
                "-p", 16
        ], getStringReader("polys.csv").text)
        Layer layer = new CsvReader().read(result)
        assertEquals(3, layer.count)
    }
}

