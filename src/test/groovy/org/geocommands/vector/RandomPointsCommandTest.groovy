package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.RandomPointsCommand.RandomPointsOptions
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The RandomPointsCommand Unit Test
 * @author Jared Erickson
 */
class RandomPointsCommandTest extends BaseTest {

    @Test
    void execute() {
        File shpFile = createTemporaryShapefile("randompoints")
        RandomPointsCommand cmd = new RandomPointsCommand()
        RandomPointsOptions options = new RandomPointsOptions(
                outputWorkspace: shpFile.absolutePath,
                number: 50,
                geometry: "0 0 100 100"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 50, shp.count
        assertEquals "Point", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        RandomPointsCommand cmd = new RandomPointsCommand()
        RandomPointsOptions options = new RandomPointsOptions(
                number: 50,
                geometry: "0 0 100 100"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 50, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }

    @Test
    void runAsCommandLine() {
        File shpFile = createTemporaryShapefile("randompoints")
        App.main([
                "vector randompoints",
                "-n", 50,
                "-g", "0 0 100 100",
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 50, shp.count
        assertEquals "Point", shp.schema.geom.typ

        String output = runApp(["vector randompoints", "-n", "50"], "POLYGON ((90 90, 90 110, 110 110, 110 90, 90 90))")
        Layer layer = getLayerFromCsv(output)
        assertEquals 50, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }

}
