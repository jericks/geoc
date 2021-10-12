package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DensifyCommand.DensifyOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The DensifyCommand Unit Test
 * @author Jared Erickson
 */
class DensifyCommandTest extends BaseTest {

    @Test
    void execute() {
        DensifyCommand cmd = new DensifyCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("densify")
        DensifyOptions options = new DensifyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                distanceTolerance: 2
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals 13, shp.features[0].geom.numPoints
        assertEquals 13, shp.features[1].geom.numPoints
        assertEquals 13, shp.features[2].geom.numPoints
        assertEquals 13, shp.features[3].geom.numPoints

    }

    @Test
    void executeWithCsv() {
        DensifyCommand cmd = new DensifyCommand()
        DensifyOptions options = new DensifyOptions(distanceTolerance: 3)
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertEquals 9, layer.features[0].geom.numPoints
        assertEquals 9, layer.features[1].geom.numPoints
        assertEquals 9, layer.features[2].geom.numPoints
        assertEquals 9, layer.features[3].geom.numPoints
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("densify")
        App.main([
                "vector densify",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-d", 2
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals 13, shp.features[0].geom.numPoints
        assertEquals 13, shp.features[1].geom.numPoints
        assertEquals 13, shp.features[2].geom.numPoints
        assertEquals 13, shp.features[3].geom.numPoints

        String output = runApp(["vector densify", "-d", "4"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertEquals 9, layer.features[0].geom.numPoints
        assertEquals 9, layer.features[1].geom.numPoints
        assertEquals 9, layer.features[2].geom.numPoints
        assertEquals 9, layer.features[3].geom.numPoints
    }
}
