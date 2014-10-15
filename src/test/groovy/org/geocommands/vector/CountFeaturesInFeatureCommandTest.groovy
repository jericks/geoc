package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.CountFeaturesInFeatureCommand.CountFeaturesInFeatureOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The CountFeaturesInFeatureCommand UnitTest
 * @author Jared Erickson
 */
class CountFeaturesInFeatureCommandTest extends BaseTest {

    private File createGrid() {
        File file = createTemporaryShapefile("grid")
        GridCommand cmd = new GridCommand()
        GridCommand.GridOptions options = new GridCommand.GridOptions(
                outputWorkspace: file.absolutePath,
                geometry: "0 0 10 10",
                columns: 10,
                rows: 10
        )
        cmd.execute(options)
        file
    }

    private File createPoints() {
        File file = createTemporaryShapefile("points")
        RandomPointsCommand cmd = new RandomPointsCommand()
        RandomPointsCommand.RandomPointsOptions options = new RandomPointsCommand.RandomPointsOptions(
                outputWorkspace: file.absolutePath,
                geometry: "0 0 10 10",
                number: 100
        )
        cmd.execute(options)
        file
    }

    @Test
    void execute() {
        CountFeaturesInFeatureCommand cmd = new CountFeaturesInFeatureCommand()
        File file = createTemporaryShapefile("grid_points")
        CountFeaturesInFeatureOptions options = new CountFeaturesInFeatureOptions(
                inputWorkspace: createGrid(),
                otherWorkspace: createPoints(),
                outputWorkspace: file
        )
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 100, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ
        assertTrue layer.schema.has("count")
        assertTrue layer.count("count > 0") > 0
    }

    @Test
    void run() {
        File file = createTemporaryShapefile("grid_points")
        runApp([
                "vector count featuresinfeature",
                "-i", createGrid().absolutePath,
                "-k", createPoints().absolutePath,
                "-o", file.absolutePath,
                "-f", "thecount"
        ], "")
        Layer layer = new Shapefile(file)
        assertEquals 100, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ
        assertTrue layer.schema.has("thecount")
        assertTrue layer.count("thecount > 0") > 0
    }

}
