package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.GraticuleRectangleCommand.GraticuleRectangleOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The GraticuleRectangleCommand Unit Test
 * @author Jared Erickson
 */
class GraticuleRectangleCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryShapefile("grid")
        GraticuleRectangleOptions options = new GraticuleRectangleOptions(
                outputWorkspace: file,
                outputLayer: "grid",
                width: 10,
                height: 20,
                geometry: "-180,-90,180,90"
        )
        GraticuleRectangleCommand cmd = new GraticuleRectangleCommand()
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 324, layer.count
    }

    @Test void run() {
        String results = runApp([
                "vector graticule rectangle",
                "-w", 10,
                "-h", 20,
                "-g", "-180,-90,180,90"
        ],"")
        Layer layer = getLayerFromCsv(results)
        assertEquals 324, layer.count
    }
}
