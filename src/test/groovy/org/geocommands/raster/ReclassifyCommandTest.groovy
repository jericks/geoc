package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.raster.ReclassifyCommand.ReclassifyOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The ReclassifyCommand Unit Test
 * @author Jared Erickson
 */
class ReclassifyCommandTest extends BaseTest {

    // geoc raster reclassify -i raster.tif -o raster_reclass.tif -r 49-100=1 -r 100-256=255

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster", "tif")
        ReclassifyCommand command = new ReclassifyCommand()
        ReclassifyOptions options = new ReclassifyOptions(
            inputRaster: inFile,
            outputRaster: outFile,
            ranges: [
                "49-100=1",
                "100-256=255"
            ]
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        assertEquals(255, outRaster.getValue(new Point(-22.2352941176, 18.5294117647)), 0.1)
        assertEquals(1, outRaster.getValue(new Point(-90.2887700535, -63.6737967914)), 0.1)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ReclassifyCommand command = new ReclassifyCommand()
        ReclassifyOptions options = new ReclassifyOptions(
                ranges: [
                        "0-185=1",
                        "185-187=120"
                ]
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(writer.toString())), new Projection("EPSG:4326"))
        assertNotNull(outRaster)
        assertEquals(1, outRaster.getValue(new Point(-174.890659341, 85.6168131868)), 0.1)
        assertEquals(120, outRaster.getValue(new Point(-175.57967033, 82.5779120879)), 0.1)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster", ".tif")
        runApp([
                "raster reclassify",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-r", "49-100=1",
                "-r", "100-256=255"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        assertEquals(255, outRaster.getValue(new Point(-22.2352941176, 18.5294117647)), 0.1)
        assertEquals(1, outRaster.getValue(new Point(-90.2887700535, -63.6737967914)), 0.1)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster reclassify",
                "-r", "0-185=1",
                "-r", "185-187=120"
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(result)), new Projection("EPSG:4326"))
        assertNotNull(outRaster)
        assertEquals(1, outRaster.getValue(new Point(-174.890659341, 85.6168131868)), 0.1)
        assertEquals(120, outRaster.getValue(new Point(-175.57967033, 82.5779120879)), 0.1)
    }
}