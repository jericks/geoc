package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The CentroidCommand Unit Test
 * @author Jared Erickson
 */
class CentroidCommandTest extends BaseTest {

    @Test
    void execute() {
        CentroidCommand cmd = new CentroidCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        CentroidCommand.CentroidOptions options = new CentroidCommand.CentroidOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        CentroidCommand cmd = new CentroidCommand()
        CentroidCommand.CentroidOptions options = new CentroidCommand.CentroidOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector centroid",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ

        String output = runApp(["vector centroid"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }
}
