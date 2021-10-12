package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.MinimumRectanglesCommand.MinimumRectanglesOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The MinimumRectanglesCommand Unit Test
 * @author Jared Erickson
 */
class MinimumRectanglesCommandTest extends BaseTest {

    @Test
    void execute() {
        MinimumRectanglesCommand cmd = new MinimumRectanglesCommand()
        File file = getResource("polys.properties")
        File shpFile = createTemporaryShapefile("minrects")
        MinimumRectanglesOptions options = new MinimumRectanglesOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        MinimumRectanglesCommand cmd = new MinimumRectanglesCommand()
        MinimumRectanglesOptions options = new MinimumRectanglesOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polys.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polys.properties")
        File shpFile = createTemporaryShapefile("minrects")
        App.main([
                "vector minrects",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector minrects"], readCsv("polys.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }
}