package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.EnvelopeCommand.EnvelopeOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The EnvelopeCommand Unit Test
 * @author Jared Erickson
 */
class EnvelopeCommandTest extends BaseTest {

    @Test
    void execute() {
        EnvelopeCommand cmd = new EnvelopeCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("envelope")
        EnvelopeOptions options = new EnvelopeOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 5, shp.features[0].geom.numPoints
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        EnvelopeCommand cmd = new EnvelopeCommand()
        EnvelopeOptions options = new EnvelopeOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals 5, layer.features[0].geom.numPoints
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("envelope")
        App.main([
                "vector envelope",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 5, shp.features[0].geom.numPoints
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector envelope"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals 5, layer.features[0].geom.numPoints
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
