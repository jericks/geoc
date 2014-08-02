package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.MinimumBoundingCirclesCommand.MinimumBoundingCirclesOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The MinimumBoundingCirclesCommand Unit Test
 * @author Jared Erickson
 */
class MinimumBoundingCirclesCommandTest extends BaseTest {

    @Test
    void execute() {
        MinimumBoundingCirclesCommand cmd = new MinimumBoundingCirclesCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("envelopes")
        MinimumBoundingCirclesOptions options = new MinimumBoundingCirclesOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        MinimumBoundingCirclesCommand cmd = new MinimumBoundingCirclesCommand()
        MinimumBoundingCirclesOptions options = new MinimumBoundingCirclesOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("envelopes")
        App.main([
                "vector mincircles",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector mincircles"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }
}