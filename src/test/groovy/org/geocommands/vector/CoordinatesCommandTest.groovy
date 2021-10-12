package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.CoordinatesCommand.CoordinatesOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The CoordinatesCommand UniTest
 * @author Jared Erickson
 */
class CoordinatesCommandTest extends BaseTest {

    @Test
    void execute() {
        CoordinatesCommand cmd = new CoordinatesCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons_coordinates")
        CoordinatesOptions options = new CoordinatesOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 20, shp.count
        assertEquals "Point", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        CoordinatesCommand cmd = new CoordinatesCommand()
        CoordinatesOptions options = new CoordinatesOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 20, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons_coordinates")
        App.main([
                "vector coordinates",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 20, shp.count
        assertEquals "Point", shp.schema.geom.typ

        String output = runApp(["vector coordinates"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 20, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }
}
