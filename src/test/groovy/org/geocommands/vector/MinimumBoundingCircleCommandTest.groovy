package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.MinimumBoundingCircleCommand.MinimumBoundingCircleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The MinimumBoundingCircleCommand Unit Test
 * @author Jared Erickson
 */
class MinimumBoundingCircleCommandTest extends BaseTest {

    @Test void execute() {
        MinimumBoundingCircleCommand cmd = new MinimumBoundingCircleCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("minboundingcircle")
        MinimumBoundingCircleOptions options = new MinimumBoundingCircleOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test void executeWithCsv() {
        MinimumBoundingCircleCommand cmd = new MinimumBoundingCircleCommand()
        MinimumBoundingCircleOptions options = new MinimumBoundingCircleOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("minboundingcircle")
        App.main([
            "vector mincircle",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector mincircle"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
