package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.vector.SpatialJoinCommand.SpatialJoinOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The SpatialJoinCommand Unit Test
 * @author Jared Erickson
 */
class SpatialJoinCommandTest extends BaseTest {

    @Test void execute() {
        SpatialJoinCommand cmd = new SpatialJoinCommand()
        File outFile = createTemporaryShapefile("joined")
        SpatialJoinOptions options = new SpatialJoinOptions(
                inputWorkspace: getResource("points.properties"),
                otherWorkspace: getResource("polygons.properties"),
                outputWorkspace: outFile.absolutePath,
                fields: [ "row", "col" ]
        )
        cmd.execute(options)
        Layer layer = new Shapefile(outFile)
        assertEquals(3, layer.count)
        assertTrue(layer.schema.has("row"))
        assertTrue(layer.schema.has("col"))
        layer.eachFeature { Feature f ->
            if (f['name'] == 'Number 1' || f['name'] == 'Number 2') {
                assertNull f['row']
                assertNull f['col']
            } else {
                assertEquals(1, f['row'])
                assertEquals(0, f['col'])
            }
        }
    }

    @Test void run() {
        String result = runApp([
                "vector join spatial",
                "-k", getResource("polygons.properties"),
                "-f", "row",
                "-f", "col"
        ], getStringReader("points.csv").text)
        Layer layer = getLayerFromCsv(result)
        assertEquals(3, layer.count)
        assertTrue(layer.schema.has("row"))
        assertTrue(layer.schema.has("col"))
        layer.eachFeature { Feature f ->
            if (f['name'] == 'Number 1' || f['name'] == 'Number 2') {
                assertEquals(null, f['row'])
                assertEquals(null, f['col'])
            } else {
                assertEquals(1, f['row'])
                assertEquals(0, f['col'])
            }
        }
    }

}
