package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.OctagonalEnvelopeCommand.OctagonalEnvelopeOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The OctagonalEnvelopeCommand Unit Test
 * @author Jared Erickson
 */
class OctagonalEnvelopeCommandTest extends BaseTest {

    @Test
    void execute() {
        OctagonalEnvelopeCommand cmd = new OctagonalEnvelopeCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("octagonalenv")
        OctagonalEnvelopeOptions options = new OctagonalEnvelopeOptions(
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
        OctagonalEnvelopeCommand cmd = new OctagonalEnvelopeCommand()
        OctagonalEnvelopeOptions options = new OctagonalEnvelopeOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("octagonalenv")
        App.main([
                "vector octagonalenvelope",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector octagonalenvelope"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
