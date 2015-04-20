package org.geocommands.raster

import geoscript.layer.Layer
import org.geocommands.raster.ExtractFootprintCommand.ExtractFootprintOptions
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The ExtractFootprintCommand Unit Test
 * @author Jared Erickson
 */
class ExtractFootprintCommandTest extends BaseTest {

    @Test void execute() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("points", "shp")
        ExtractFootprintCommand command = new ExtractFootprintCommand()
        ExtractFootprintOptions options = new ExtractFootprintOptions(
                inputRaster: inFile,
                outputWorkspace: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        Layer layer = new Shapefile(outFile)
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("MultiPolygon"))
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("points", "shp")
        runApp([
                "raster extractfootprint",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ],"")

        Layer layer = new Shapefile(outFile)
        assertTrue(layer.count > 0)
        assertTrue(layer.schema.has("the_geom"))
        assertTrue(layer.schema.has("value"))
        assertTrue(layer.schema.geom.typ.equalsIgnoreCase("MultiPolygon"))
    }

}
