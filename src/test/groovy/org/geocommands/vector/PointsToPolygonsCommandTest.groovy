package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.PointsToPolygonsCommand.PointsToPolygonsOptions
import org.junit.Test

import static org.junit.Assert.*

class PointsToPolygonsCommandTest extends BaseTest {

    @Test void executeToPolygon() {
        PointsToPolygonsCommand cmd = new PointsToPolygonsCommand()
        File inFile = getResource("points2polygons.properties")
        File outFile = createTemporaryShapefile("points2polygons")
        PointsToPolygonsOptions options = new PointsToPolygonsOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile.absolutePath
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals 1, layer.count
        assertEquals 1, layer.first()["id"]
        assertEquals 11, layer.first().geom.points.size()
    }

    @Test void executeToPolygons() {
        PointsToPolygonsCommand cmd = new PointsToPolygonsCommand()
        File inFile = getResource("points2polygons.properties")
        File outFile = createTemporaryShapefile("points2polygons")
        PointsToPolygonsOptions options = new PointsToPolygonsOptions(
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
        assertEquals 4, layer.features[0].geom.points.size()
        // 2
        assertEquals 2, layer.features[1]["id"]
        assertEquals 2, layer.features[1]["group"]
        assertEquals 5, layer.features[1].geom.points.size()
        // 3
        assertEquals 3, layer.features[2]["id"]
        assertEquals 3, layer.features[2]["group"]
        assertEquals 4, layer.features[2].geom.points.size()
    }

    @Test void runToPolygon() {
        String csv = runApp([
                "vector points2polygons"
        ], getStringReader("points2polygons.csv").text)
        Layer layer = getLayerFromCsv(csv)
        assertEquals 1, layer.count
        assertEquals 1, layer.first()["id"]
        assertEquals 11, layer.first().geom.points.size()
    }

    @Test void runToPolygons() {
        String csv = runApp([
                "vector points2polygons",
                "-s", "id",
                "-g", "group"
        ], getStringReader("points2polygons.csv").text)
        Layer layer = getLayerFromCsv(csv)
        assertEquals 3, layer.count
        // 1
        assertEquals 1, layer.features[0]["id"]
        assertEquals 1, layer.features[0]["group"]
        assertEquals 4, layer.features[0].geom.points.size()
        // 2
        assertEquals 2, layer.features[1]["id"]
        assertEquals 2, layer.features[1]["group"]
        assertEquals 5, layer.features[1].geom.points.size()
        // 3
        assertEquals 3, layer.features[2]["id"]
        assertEquals 3, layer.features[2]["group"]
        assertEquals 4, layer.features[2].geom.points.size()
    }

}
