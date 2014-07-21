package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.ResampleCommand.ResampleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The ResampleCommand Unit Test
 * @author Jared Erickson
 */
class ResampleCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki", "tif")
        ResampleCommand command = new ResampleCommand()
        ResampleOptions options = new ResampleOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                bounds: "1166291.0260847565,823060.0090852415,1167431.8522748263,824326.3820666744",
                size: "861, 944"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)
        assertEquals(Bounds.fromString("1166291.0260847565,823060.0090852415,1167431.8522748263,824326.3820666744,EPSG:2927"), outRaster.bounds)
        assertEquals(861, outRaster.size[0])
        assertEquals(944, outRaster.size[1])
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ResampleCommand command = new ResampleCommand()
        ResampleOptions options = new ResampleOptions(
                inputProjection: "EPSG:4326",
                bounds: "-166.0,71.6,-163.6,76.0,EPSG:4326",
                size: "16,21"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = format.read(new Projection("EPSG:4326"))
        assertNotNull(outRaster)
        assertEquals(Bounds.fromString("-166.0,71.6,-163.6,74.75,EPSG:4326"), outRaster.bounds)
        assertEquals(16, outRaster.size[0])
        assertEquals(21, outRaster.size[1])
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki", ".tif")
        runApp([
                "raster resample",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-b", "1166291.0260847565,823060.0090852415,1167431.8522748263,824326.3820666744,EPSG:2927",
                "-s", "861, 944"
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)
        assertEquals(Bounds.fromString("1166291.0260847565,823060.0090852415,1167431.8522748263,824326.3820666744,EPSG:2927"), outRaster.bounds)
        assertEquals(861, outRaster.size[0])
        assertEquals(944, outRaster.size[1])
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster resample",
                "-b", "-166.0,71.6,-163.6,76.0,EPSG:4326",
                "-s", "16,21"
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = format.read(new Projection("EPSG:4326"))
        assertNotNull(outRaster)
        assertEquals(Bounds.fromString("-166.0,71.6,-163.6,74.75,EPSG:4326"), outRaster.bounds)
        assertEquals(16, outRaster.size[0])
        assertEquals(21, outRaster.size[1])
    }

}