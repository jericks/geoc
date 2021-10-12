package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.LargestEmptyCircleCommand.LargestEmptyCircleOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The LargestEmptyCircleCommand Unit Test
 * @author Jared Erickson
 */
class LargestEmptyCircleCommandTest extends BaseTest {

    @Test
    void execute() {
        LargestEmptyCircleCommand cmd = new LargestEmptyCircleCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("circle")
        LargestEmptyCircleOptions options = new LargestEmptyCircleOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 1, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        LargestEmptyCircleCommand cmd = new LargestEmptyCircleCommand()
        LargestEmptyCircleOptions options = new LargestEmptyCircleOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals 1, layer.features[0].geom.numGeometries
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("circle")
        App.main([
                "vector largestemptycircle",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 1, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector largestemptycircle"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals 1, layer.features[0].geom.numGeometries
        assertEquals "Polygon", layer.schema.geom.typ
    }
}
