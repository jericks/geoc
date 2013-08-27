package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.PointStackerCommand.PointStackerOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The PointStackerCommand Unit Test
 * @author Jared Erickson
 */
class PointStackerCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes_stacked")
        PointStackerCommand cmd = new PointStackerCommand()
        PointStackerOptions options = new PointStackerOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile.absolutePath,
                cellSize: 5,
                width: 800,
                height: 600
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile layer = new Shapefile(shpFile)
        assertEquals 15, layer.count("count = 1")
        assertEquals 8, layer.count("count > 1")
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("earthquakes.csv")
        StringWriter writer = new StringWriter()
        PointStackerCommand cmd = new PointStackerCommand()
        PointStackerOptions options = new PointStackerOptions(
                cellSize: 5,
                width: 800,
                height: 600
        )
        cmd.execute(options, reader, writer)
        Layer layer = new CsvReader().read(writer.toString())
        assertEquals 15, layer.count("count = 1")
        assertEquals 8, layer.count("count > 1")
    }

    @Test
    void runWithFiles() {
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes_stacked")
        runApp([
                "vector pointstacker",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-c", "5",
                "-w", "800",
                "-h", "600"
        ], "")
        Shapefile layer = new Shapefile(shpFile)
        assertEquals 15, layer.count("count = 1")
        assertEquals 8, layer.count("count > 1")
    }

    @Test
    void runWithStrings() {
        StringReader reader = getStringReader("earthquakes.csv")
        String result = runApp([
                "vector pointstacker",
                "-c", "5",
                "-w", "800",
                "-h", "600"
        ], reader.text)
        Layer layer = new CsvReader().read(result)
        assertEquals 15, layer.count("count = 1")
        assertEquals 8, layer.count("count > 1")
    }
}
