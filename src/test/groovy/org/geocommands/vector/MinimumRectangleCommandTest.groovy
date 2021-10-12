package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.MinimumRectangleCommand.MinimumRectangleOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The MinimumRectangleCommand Unit Test
 * @author Jared Erickson
 */
class MinimumRectangleCommandTest extends BaseTest {

    @Test
    void execute() {
        MinimumRectangleCommand cmd = new MinimumRectangleCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("minrect")
        MinimumRectangleOptions options = new MinimumRectangleOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        MinimumRectangleCommand cmd = new MinimumRectangleCommand()
        MinimumRectangleOptions options = new MinimumRectangleOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("minrect")
        App.main([
                "vector minrect",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector minrect"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
