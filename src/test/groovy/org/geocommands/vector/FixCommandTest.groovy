package org.geocommands.vector

import geoscript.geom.LineString
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.*

/**s
 * The FixCommand Unit Test
 * @author Jared Erickson
 */
class FixCommandTest extends BaseTest {

    @Test
    void execute() {
        FixCommand cmd = new FixCommand()
        File file = getResource("fixable.properties")
        File shpFile = createTemporaryShapefile("points")
        FixCommand.FixOptions options = new FixCommand.FixOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        assertEquals "MultiLineString", shp.schema.geom.typ
        assertEquals "MULTILINESTRING ((0 0, 1 1))", shp.features[0].geom.wkt
        assertEquals "MULTILINESTRING ((1 1, 0 0, 2 2))", shp.features[1].geom.wkt
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("fixable.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
            "vector fix",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        assertEquals "MultiLineString", shp.schema.geom.typ
        assertEquals "MULTILINESTRING ((0 0, 1 1))", shp.features[0].geom.wkt
        assertEquals "MULTILINESTRING ((1 1, 0 0, 2 2))", shp.features[1].geom.wkt

        String output = runApp(["vector fix"], readCsv("fixable.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 2, layer.count
        layer.eachFeature { assertTrue it.geom instanceof LineString }
        assertEquals "LINESTRING (0 0, 1 1)", layer.features[0].geom.wkt
        assertEquals "LINESTRING (1 1, 0 0, 2 2)", layer.features[1].geom.wkt
    }
}
