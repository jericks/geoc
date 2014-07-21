package org.geocommands.raster

import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.ProjectCommand.ProjectOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The ProjectCommand Unit Test
 * @author Jared Erickson
 */
class ProjectCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_projected", ".tif")
        ProjectCommand command = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                outputProjection: "EPSG:4326"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals("EPSG:4326", raster.proj.id)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ProjectCommand command = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                inputProjection: "EPSG:4326",
                outputProjection: "EPSG:2927"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster raster = format.read(new Projection("EPSG:2927"))
        assertNotNull(raster)
        assertEquals("EPSG:2927", raster.proj.id)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_projected", ".tif")
        runApp([
                "raster project",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-t", "EPSG:4326"
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals("EPSG:4326", raster.proj.id)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String result = runApp([
                "raster project",
                "-t", "EPSG:2927"
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read(new Projection("EPSG:2927"))
        assertNotNull(raster)
        assertEquals("EPSG:2927", raster.proj.id)
    }
}
