package org.geocommands.vector

import geoscript.geom.Polygon
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test

/**
 * The ConvexHullCommand UniTest
 * @author Jared Erickson
 */
class ConvexHullCommandTest extends BaseTest {

    @Test
    void execute() {
        ConvexHullCommand cmd = new ConvexHullCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points_convexhull")
        LayerInOutOptions options = new LayerInOutOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 1, shp.count
        org.junit.Assert.assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        ConvexHullCommand cmd = new ConvexHullCommand()
        LayerInOutOptions options = new LayerInOutOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        org.junit.Assert.assertEquals 1, layer.count
        layer.eachFeature { org.junit.Assert.assertTrue it.geom instanceof Polygon }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points_convexhull")
        App.main([
                "vector convexhull",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 1, shp.count
        org.junit.Assert.assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector convexhull"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        org.junit.Assert.assertEquals 1, layer.count
        layer.eachFeature { org.junit.Assert.assertTrue it.geom instanceof Polygon }
    }
}
