package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.InteriorPointCommand.InteriorPointOptions
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The InteriorPointCommand Unit Test
 * @author Jared Erickson
 */
class InteriorPointCommandTest extends BaseTest {

    @Test
    void execute() {
        InteriorPointCommand cmd = new InteriorPointCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("interiorpoints")
        InteriorPointOptions options = new InteriorPointOptions(
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
        InteriorPointCommand cmd = new InteriorPointCommand()
        InteriorPointOptions options = new InteriorPointOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("interiorpoints")
        App.main([
                "vector interiorpoint",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ

        String output = runApp(["vector interiorpoint"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Point }
    }
}
