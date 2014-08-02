package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.SingleToMultipleCommand.SingleToMultipleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The SingleToMultipleCommand Unit Test
 * @author Jared Erickson
 */
class SingleToMultipleCommandTest extends BaseTest {

    @Test
    void execute() {
        SingleToMultipleCommand cmd = new SingleToMultipleCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("multiple")
        SingleToMultipleOptions options = new SingleToMultipleOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 3, shp.features[0].geom.numGeometries
        assertEquals "MultiPoint", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        SingleToMultipleCommand cmd = new SingleToMultipleCommand()
        SingleToMultipleOptions options = new SingleToMultipleOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals 3, layer.features[0].geom.numGeometries
        assertEquals "MultiPoint", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("multiple")
        String output = runApp([
                "vector single2multiple",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ], "")
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 3, shp.features[0].geom.numGeometries
        assertEquals "MultiPoint", shp.schema.geom.typ

        output = runApp(["vector single2multiple"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals 3, layer.features[0].geom.numGeometries
        assertEquals "MultiPoint", layer.schema.geom.typ
    }
}