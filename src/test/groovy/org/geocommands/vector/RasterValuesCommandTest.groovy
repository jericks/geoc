package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.RasterValuesCommand.RasterValuesOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The RasterValuesCommand Unit Test
 * @author Jared Erickson
 */
class RasterValuesCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inVectorFile = getResource("alki_points.properties")
        File inRasterFile = getResource("alki.tif")
        File outVectorFile = createTemporaryShapefile("alki_points_values")
        RasterValuesOptions options = new RasterValuesOptions(
                inputWorkspace: inVectorFile.absolutePath,
                inputRaster: inRasterFile.absolutePath,
                outputWorkspace: outVectorFile.absolutePath,
        )
        RasterValuesCommand cmd = new RasterValuesCommand()
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer inLayer = new Property(inVectorFile)
        Layer outLayer = new Shapefile(outVectorFile)
        assertEquals inLayer.count, outLayer.count
        assertFalse inLayer.schema.has("value")
        assertTrue outLayer.schema.has("value")
        assertEquals 37, outLayer.count("value IS NOT NULL")
        assertEquals 63, outLayer.count("value IS NULL")
        assertEquals 222.0, outLayer.first(filter: "value IS NOT NULL")['value'], 0.1
    }

    @Test
    void executeWithStrings() {
        StringReader inVectorReader = getStringReader("alki_points.csv")
        File inRasterFile = getResource("alki.tif")
        StringWriter writer = new StringWriter()
        RasterValuesOptions options = new RasterValuesOptions(
                inputRaster: inRasterFile.absolutePath,
                valueFieldName: "raster_value"
        )
        RasterValuesCommand cmd = new RasterValuesCommand()
        cmd.execute(options, inVectorReader, writer)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("alki_points.csv").text)
        Layer outLayer = csvReader.read(writer.toString())
        assertEquals inLayer.count, outLayer.count
        assertFalse inLayer.schema.has("raster_value")
        assertTrue outLayer.schema.has("raster_value")
        assertEquals 37, outLayer.count("raster_value <> ''")
        assertEquals 63, outLayer.count("raster_value = ''")
        assertEquals 222.0, outLayer.first(filter: "raster_value <> ''")['raster_value'] as double, 0.1
    }

    @Test
    void runWithFiles() {
        File inVectorFile = getResource("alki_points.properties")
        File inRasterFile = getResource("alki.tif")
        File outVectorFile = createTemporaryShapefile("alki_points_values")
        runApp([
                "vector raster values",
                "-i", inVectorFile.absolutePath,
                "-s", inRasterFile.absolutePath,
                "-o", outVectorFile.absolutePath
        ], "")
        Layer inLayer = new Property(inVectorFile)
        Layer outLayer = new Shapefile(outVectorFile)
        assertEquals inLayer.count, outLayer.count
        assertFalse inLayer.schema.has("value")
        assertTrue outLayer.schema.has("value")
        assertEquals 37, outLayer.count("value IS NOT NULL")
        assertEquals 63, outLayer.count("value IS NULL")
        assertEquals 222.0, outLayer.first(filter: "value IS NOT NULL")['value'], 0.1
    }

    @Test
    void runWithStrings() {
        StringReader inVectorReader = getStringReader("alki_points.csv")
        File inRasterFile = getResource("alki.tif")
        String result = runApp([
                "vector raster values",
                "-s", inRasterFile.absolutePath,
                "-n", "raster_value"
        ], inVectorReader.text)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("alki_points.csv").text)
        Layer outLayer = csvReader.read(result)
        assertEquals inLayer.count, outLayer.count
        assertFalse inLayer.schema.has("raster_value")
        assertTrue outLayer.schema.has("raster_value")
        assertEquals 37, outLayer.count("raster_value <> ''")
        assertEquals 63, outLayer.count("raster_value = ''")
        assertEquals 222.0, outLayer.first(filter: "raster_value <> ''")['raster_value'] as double, 0.1
    }
}
