package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.vector.PointsToLinesCommand.PointsToLineOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

class PointsToLinesCommandTest extends BaseTest {

    @Test void executeToLine() {
        PointsToLinesCommand cmd = new PointsToLinesCommand()
        File inFile = getResource("points2lines.properties")
        File outFile = createTemporaryShapefile("points2lines")
        PointsToLineOptions options = new PointsToLineOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals 1, layer.count
        assertEquals 1, layer.first()["id"]
        assertEquals 9, layer.first().geom.points.size()
    }

    @Test void executeToLines() {
        PointsToLinesCommand cmd = new PointsToLinesCommand()
        File inFile = getResource("points2lines.properties")
        File outFile = createTemporaryShapefile("points2lines")
        PointsToLineOptions options = new PointsToLineOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath,
                orderByField: "id",
                groupByField: "group"
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals 3, layer.count
        // 1
        assertEquals 1, layer.features[0]["id"]
        assertEquals 1, layer.features[0]["group"]
        assertEquals 3, layer.features[0].geom.points.size()
        // 2
        assertEquals 2, layer.features[1]["id"]
        assertEquals 2, layer.features[1]["group"]
        assertEquals 4, layer.features[1].geom.points.size()
        // 3
        assertEquals 3, layer.features[2]["id"]
        assertEquals 3, layer.features[2]["group"]
        assertEquals 2, layer.features[2].geom.points.size()
    }

    @Test void runToLine() {
        String csv = runApp([
                "vector points2lines"
        ], getStringReader("points2lines.csv").text)
        Layer layer = getLayerFromCsv(csv)
        assertEquals 1, layer.count
        assertEquals 1, layer.first()["id"]
        assertEquals 9, layer.first().geom.points.size()
    }

    @Test void runToLines() {
        String csv = runApp([
                "vector points2lines",
                "-s", "id",
                "-g", "group"
        ], getStringReader("points2lines.csv").text)
        Layer layer = getLayerFromCsv(csv)
        assertEquals 3, layer.count
        // 1
        assertEquals 1, layer.features[0]["id"]
        assertEquals 1, layer.features[0]["group"]
        assertEquals 3, layer.features[0].geom.points.size()
        // 2
        assertEquals 2, layer.features[1]["id"]
        assertEquals 2, layer.features[1]["group"]
        assertEquals 4, layer.features[1].geom.points.size()
        // 3
        assertEquals 3, layer.features[2]["id"]
        assertEquals 3, layer.features[2]["group"]
        assertEquals 2, layer.features[2].geom.points.size()
    }

}
