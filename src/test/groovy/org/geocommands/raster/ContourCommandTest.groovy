package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.raster.ContourCommand.ContourOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * The ContourCommand Unit Test
 * @author Jared Erickson
 */
class ContourCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("contours", "shp")
        ContourCommand command = new ContourCommand()
        ContourCommand.ContourOptions options = new ContourCommand.ContourOptions(
                inputRaster: inFile,
                outputWorkspace: outFile,
                band: 0,
                levels: [10,20,50,100,200]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multilinestring"))
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ContourCommand command = new ContourCommand()
        ContourCommand.ContourOptions options = new ContourCommand.ContourOptions(
            band: 0,
            levels: [184,185,186,187]
        )
        command.execute(options, reader, writer)

        Layer layer = new CsvReader().read(writer.toString())
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("linestring"))
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("contours", "shp")
        runApp([
                "raster contour",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-b", "0",
                "-v","10",
                "-v","20",
                "-v","50",
                "-v","100",
                "-v","200"
        ], "")

        Shapefile shp = new Shapefile(outFile)
        assertTrue(shp.count > 0)
        assertTrue(shp.schema.has("the_geom"))
        assertTrue(shp.schema.has("value"))
        assertTrue(shp.schema.geom.typ.equalsIgnoreCase("multilinestring"))
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster contour",
                "-b", "0",
                "-v","184",
                "-v","185",
                "-v","186",
                "-v","187"
        ], reader.text)

        Layer layer = new CsvReader().read(result)
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("linestring"))
    }
}
