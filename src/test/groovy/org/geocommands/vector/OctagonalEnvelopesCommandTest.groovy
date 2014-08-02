package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.OctagonalEnvelopesCommand.OctagonalEnvelopesOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The OctagonalEnvelopesCommand Unit Test
 * @author Jared Erickson
 */
class OctagonalEnvelopesCommandTest extends BaseTest {

    @Test
    void execute() {
        OctagonalEnvelopesCommand cmd = new OctagonalEnvelopesCommand()
        File file = getResource("polys.properties")
        File shpFile = createTemporaryShapefile("octagonalenvelopes")
        OctagonalEnvelopesOptions options = new OctagonalEnvelopesOptions(
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
        OctagonalEnvelopesCommand cmd = new OctagonalEnvelopesCommand()
        OctagonalEnvelopesOptions options = new OctagonalEnvelopesOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polys.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polys.properties")
        File shpFile = createTemporaryShapefile("octagonalenvelopes")
        App.main([
                "vector octagonalenvelopes",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector octagonalenvelopes"], readCsv("polys.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }
}