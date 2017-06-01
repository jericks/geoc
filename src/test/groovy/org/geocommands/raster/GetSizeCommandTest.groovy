package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.GetSizeCommand.GetSizeOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The GetSizeCommand Unit Test
 * @author Jared Erickson
 */
class GetSizeCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        GetSizeCommand command = new GetSizeCommand()
        GetSizeOptions options = new GetSizeOptions(
                inputRaster: inFile
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = "900,450"
        String actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void executeWithString() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        GetSizeCommand command = new GetSizeCommand()
        GetSizeOptions options = new GetSizeOptions(
                inputProjection: "EPSG:4326"
        )
        command.execute(options, reader, writer)
        String expected = "6,11"
        String actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithFile() {
        File inFile = getResource("alki.tif")
        String actual = runApp([
                "raster size",
                "-i", inFile.absolutePath
        ], "")
        String expected = "761,844" + NEW_LINE
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        String actual = runApp([
                "raster size",
                "-p", "EPSG:4326"

        ], reader.text)
        String expected = "7,5" + NEW_LINE
        assertEquals(expected, actual)
    }
}
