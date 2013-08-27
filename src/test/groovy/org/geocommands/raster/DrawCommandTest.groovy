package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.DrawCommand.DrawOptions
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * The DrawCommand UnitTest
 * @author Jared Erickson
 */
class DrawCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("map", "jpeg")
        DrawCommand command = new DrawCommand()
        DrawOptions options = new DrawOptions(
                inputRaster: inFile,
                file: outFile,
                type: "jpeg"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        assertTrue(outFile.exists())
        assertTrue(outFile.size() > 0)
    }

    @Test
    void executeWithStringToFile() {
        Reader reader = getStringReader("raster.asc")
        File outFile = createTemporaryFile("map", "png")
        File sldFile = getResource("raster_colormap.sld")
        DrawCommand command = new DrawCommand()
        DrawOptions options = new DrawOptions(
                file: outFile,
                type: "png",
                width: 400,
                height: 400,
                sldFile: sldFile
        )
        command.execute(options, reader, new StringWriter())

        assertTrue(outFile.exists())
        assertTrue(outFile.size() > 0)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("map", "png")
        File sldFile = getResource("raster_colormap.sld")
        runApp([
                "raster draw",
                "-i", inFile.absolutePath,
                "-f", outFile.absolutePath,
                "-t", "png",
                "-w", "800",
                "-h", "600",
                "-s", sldFile.absolutePath
        ], "")

        assertTrue(outFile.exists())
        assertTrue(outFile.size() > 0)
    }

    @Test
    void runAsCommandLineWithStringToFile() {
        Reader reader = getStringReader("raster.asc")
        File outFile = createTemporaryFile("map", "jpg")
        runApp([
                "raster draw",
                "-f", outFile.absolutePath,
                "-t", "jpg",
                "-w", "800",
                "-h", "600"
        ], reader.text)

        assertTrue(outFile.exists())
        assertTrue(outFile.size() > 0)
    }
}
