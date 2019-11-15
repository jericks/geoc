package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.GraticuleOvalCommand.GraticuleOvalOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The GraticuleOvalCommand Unit Test
 * @author Jared Erickson
 */
class GraticuleOvalCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryShapefile("grid")
        GraticuleOvalOptions options = new GraticuleOvalOptions(
                outputWorkspace: file,
                outputLayer: "grid",
                length: 10,
                geometry: "-180,-90,180,90"
        )
        GraticuleOvalCommand cmd = new GraticuleOvalCommand()
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 648, layer.count
    }

    @Test void run() {
        String results = runApp([
                "vector graticule oval",
                "-l", 10,
                "-g", "-180,-90,180,90"
        ],"")
        Layer layer = getLayerFromCsv(results)
        assertEquals 648, layer.count
    }
}
