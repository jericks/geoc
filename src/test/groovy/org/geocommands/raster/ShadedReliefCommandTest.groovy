package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class ShadedReliefCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki", "tif")
        ShadedReliefCommand command = new ShadedReliefCommand()
        ShadedReliefCommand.ShadedReliefOptions options = new ShadedReliefCommand.ShadedReliefOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                scale: 1.0,
                altitude: 45.5,
                azimuth: 15.0
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ShadedReliefCommand command = new ShadedReliefCommand()
        ShadedReliefCommand.ShadedReliefOptions options = new ShadedReliefCommand.ShadedReliefOptions(
                inputProjection: "EPSG:4326",
                scale: 1.0,
                altitude: 45.5,
                azimuth: 15.0
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster outRaster = format.read(new Projection("EPSG:4326"))
        assertNotNull(outRaster)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki", ".tif")
        runApp([
                "raster shadedrelief",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-s", "1.0",
                "-a", "45.4",
                "-m", "15.0"
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster outRaster = format.read()
        assertNotNull(outRaster)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster shadedrelief",
                "-s", "1.0",
                "-a", "45.4",
                "-m", "15.0"
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster outRaster = format.read(new Projection("EPSG:4326"))
        assertNotNull(outRaster)
    }
    
}
