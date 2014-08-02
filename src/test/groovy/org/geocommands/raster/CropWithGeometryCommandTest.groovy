package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.CropWithGeometryCommand.CropWithGeometryOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The CropWithGeometryCommand Unit Test
 * @author Jared Erickson
 */
class CropWithGeometryCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        Geometry geometry = Geometry.fromWKT("POINT (1166476.232632274 823276.6023305996)").buffer(50)
        CropWithGeometryCommand command = new CropWithGeometryCommand()
        CropWithGeometryOptions options = new CropWithGeometryOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                geometry: geometry.wkt
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        Geometry geometry = Geometry.fromWKT("POINT (-174.80000000000004 83.8)").buffer(1.2)
        CropWithGeometryCommand command = new CropWithGeometryCommand()
        CropWithGeometryOptions options = new CropWithGeometryOptions(
                geometry: geometry.wkt
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        Geometry geometry = Geometry.fromWKT("POINT (1166476.232632274 823276.6023305996)").buffer(50)
        runApp([
                "raster crop with geometry",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-g", geometry.wkt
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = geometry.bounds
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        Geometry geometry = Geometry.fromWKT("POINT (-174.80000000000004 83.8)").buffer(1.2)
        String result = runApp([
                "raster crop with geometry",
                "-g", geometry.wkt
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read()
        assertNotNull(raster)
        Bounds bounds = geometry.bounds
        assertEquals(bounds.minX, raster.bounds.minX, 1d)
        assertEquals(bounds.maxX, raster.bounds.maxX, 1d)
        assertEquals(bounds.minY, raster.bounds.minY, 1d)
        assertEquals(bounds.maxY, raster.bounds.maxY, 1d)
    }
}
