package org.geocommands.tile

import geoscript.feature.Feature
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.tile.VectorGridCommand.VectorGridOptions
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The VectorGridCommand Unit Test
 * @author Jared Erickson
 */
class VectorGridCommandTest extends BaseTest {

    @Test
    void execute() {
        File mbtilesFile = getResource("earthquakes.mbtiles")
        File file = createTemporaryShapefile("grid")
        VectorGridCommand cmd = new VectorGridCommand()
        VectorGridOptions options = new VectorGridOptions(
                tileLayer: mbtilesFile.absolutePath,
                z: 1,
                outputWorkspace: file.absolutePath
        )
        cmd.execute(options)
        Layer layer = new Shapefile(file)
        assertEquals 4, layer.count
        ["the_geom","id","z","x","y"].each { String field ->
            assertTrue layer.schema.has(field)
        }
        layer.eachFeature { Feature f ->
            assertNotNull f.geom
            assertTrue f["id"] >= 0
            assertTrue f["z"] == 1
            assertTrue f["x"] >= 0
            assertTrue f["y"] >= 0
        }
    }

    @Test
    void run() {
        File mbtilesFile = getResource("earthquakes.mbtiles")
        File file = createTemporaryShapefile("grid")
        runApp([
           "tile vector grid",
                "-l", mbtilesFile.absolutePath,
                "-z", 1,
                "-o", file.absolutePath
        ],"")
        Layer layer = new Shapefile(file)
        assertEquals 4, layer.count
        ["the_geom","id","z","x","y"].each { String field ->
            assertTrue layer.schema.has(field)
        }
        layer.eachFeature { Feature f ->
            assertNotNull f.geom
            assertTrue f["id"] >= 0
            assertTrue f["z"] == 1
            assertTrue f["x"] >= 0
            assertTrue f["y"] >= 0
        }
    }
}
