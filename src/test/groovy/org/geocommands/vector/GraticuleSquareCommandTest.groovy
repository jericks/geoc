package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.vector.GraticuleSquareCommand.GraticuleSquareOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The GraticuleSquareCommand Unit Test
 * @author Jared Erickson
 */
class GraticuleSquareCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryShapefile("grid")
        GraticuleSquareOptions options = new GraticuleSquareOptions(
                outputWorkspace: file,
                outputLayer: "grid",
                length: 10,
                geometry: "-180,-90,180,90"
        )
        GraticuleSquareCommand cmd = new GraticuleSquareCommand()
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 648, layer.count
    }

    @Test void run() {
        String results = runApp([
                "vector graticule square",
                "-l", 10,
                "-g", "-180,-90,180,90"
        ],"")
        Layer layer = getLayerFromCsv(results)
        assertEquals 648, layer.count
    }
}
