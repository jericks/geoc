package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.GraticuleLineCommand.GraticuleLineOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The GraticuleLineCommand Unit Test
 * @author Jared Erickson
 */
class GraticuleLineCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryShapefile("grid")
        GraticuleLineOptions options = new GraticuleLineOptions(
                outputWorkspace: file,
                outputLayer: "grid",
                lineDefinitions: [
                    "vertical,2,10",
                    "vertical,1,2",
                    "horizontal,2,10",
                    "horizontal,1,2"
                ],
                geometry: "-180,-90,180,90"
        )
        GraticuleLineCommand cmd = new GraticuleLineCommand()
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 272, layer.count
    }

    @Test void run() {
        String results = runApp([
                "vector graticule Line",
                "-l", "vertical,2,10",
                "-l", "vertical,1,2",
                "-l", "horizontal,2,10",
                "-l", "horizontal,1,2",
                "-g", "-180,-90,180,90"
        ],"")
        Layer layer = getLayerFromCsv(results)
        assertEquals 272, layer.count
    }
}
