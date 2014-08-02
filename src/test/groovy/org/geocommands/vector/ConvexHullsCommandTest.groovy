package org.geocommands.vector

import geoscript.geom.Polygon
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.ConvexHullsCommand.ConvexHullsOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The ConvexHullsCommand UniTest
 * @author Jared Erickson
 */
class ConvexHullsCommandTest extends BaseTest {

    @Test
    void execute() {
        ConvexHullsCommand cmd = new ConvexHullsCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons_convexhulls")
        ConvexHullsOptions options = new ConvexHullsOptions(
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
        ConvexHullsCommand cmd = new ConvexHullsCommand()
        ConvexHullsOptions options = new ConvexHullsOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons_convexhulls")
        App.main([
                "vector convexhulls",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector convexhulls"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon }
    }
}
