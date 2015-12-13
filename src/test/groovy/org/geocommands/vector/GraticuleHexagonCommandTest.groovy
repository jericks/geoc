package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.GraticuleHexagonCommand.GraticuleHexagonOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The GraticuleHexagonCommand Unit Test
 * @author Jared Erickson
 */
class GraticuleHexagonCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryShapefile("grid")
        GraticuleHexagonOptions options = new GraticuleHexagonOptions(
                outputWorkspace: file,
                outputLayer: "grid",
                length: 10,
                geometry: "-180,-90,180,90"
        )
        GraticuleHexagonCommand cmd = new GraticuleHexagonCommand()
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 219, layer.count
    }

    @Test void run() {
        String results = runApp([
                "vector graticule hexagon",
                "-l", 10,
                "-g", "-180,-90,180,90"
        ],"")
        Layer layer = getLayerFromCsv(results)
        assertEquals 219, layer.count
    }
}
