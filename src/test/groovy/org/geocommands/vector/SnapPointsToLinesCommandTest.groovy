package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.vector.SnapPointsToLinesCommand.SnapPointsToLinesOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The SnapPointsToLinesCommand Unit Test
 * @author Jared Erickson
 */
class SnapPointsToLinesCommandTest extends BaseTest {

    @Test void executeWithFiles() {
        File outFile = createTemporaryShapefile("snapped")
        SnapPointsToLinesCommand cmd = new SnapPointsToLinesCommand()
        SnapPointsToLinesOptions options = new SnapPointsToLinesOptions(
                inputWorkspace: getResource("points.properties"),
                otherWorkspace: getResource("lines.properties"),
                outputWorkspace: outFile,
                searchDistance: 2
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals 3, layer.count
        assertEquals 3, layer.count("SNAPPED = 1")
        assertEquals 0, layer.count("SNAPPED = 0")
    }

    @Test void runWithFiles() {
        File outFile = createTemporaryShapefile("snapped")
        runApp([
                "vector snap points2lines",
                "-i", getResource("points.properties").absolutePath,
                "-k", getResource("lines.properties").absolutePath,
                "-o", outFile.absolutePath,
                "-d", 2
        ], "")
        Layer layer = new Shapefile(outFile)
        assertEquals 3, layer.count
        assertEquals 3, layer.count("SNAPPED = 1")
        assertEquals 0, layer.count("SNAPPED = 0")
    }

    @Test void execute() {
        SnapPointsToLinesCommand cmd = new SnapPointsToLinesCommand()
        SnapPointsToLinesOptions options = new SnapPointsToLinesOptions(
                otherWorkspace: getResource("lines.properties"),
                searchDistance: 2
        )
        StringReader reader = getStringReader("points.csv")
        StringWriter writer =  new StringWriter()
        cmd.execute(options, reader, writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals 3, layer.count
        assertEquals 3, layer.count("SNAPPED = 1")
        assertEquals 0, layer.count("SNAPPED = 0")
    }

    @Test void run() {
        String output = runApp([
                "vector snap points2lines",
                "-k", getResource("lines.properties").absolutePath,
                "-d", 2
        ], getStringReader("points.csv").text)
        Layer layer = new CsvReader().read(output)
        assertEquals 3, layer.count
        assertEquals 3, layer.count("SNAPPED = 1")
        assertEquals 0, layer.count("SNAPPED = 0")
    }

}
