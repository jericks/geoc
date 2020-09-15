package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.GetValueCommand.GetValueOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The GetValueCommand Unit Test
 * @author Jared Erickson
 */
class GetValueCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        GetValueCommand command = new GetValueCommand()
        GetValueOptions options = new GetValueOptions(
                inputRaster: inFile,
                x: 1,
                y: 1,
                type: "pixel"
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = "184.0" + NEW_LINE
        String actual = writer.toString()
        assertEquals(expected, actual)

        options = new GetValueOptions(
                inputRaster: inFile,
                x: 1,
                y: 1,
                type: "point"
        )
        writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        expected = "227.0" + NEW_LINE
        actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void executeWithString() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        GetValueCommand command = new GetValueCommand()
        GetValueOptions options = new GetValueOptions(
                x: 1,
                y: 1,
                type: "pixel"
        )
        command.execute(options, reader, writer)
        String expected = "184.0" + NEW_LINE
        String actual = writer.toString()
        assertEquals(expected, actual)

        reader = getStringReader("raster.asc")
        writer = new StringWriter()
        command = new GetValueCommand()
        options = new GetValueOptions(
                x: -174,
                y: 83,
                type: "point"
        )
        command.execute(options, reader, writer)
        expected = "185.0" + NEW_LINE
        actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithFile() {
        File inFile = getResource("raster.tif")
        String actual = runApp([
                "raster get value",
                "-i", inFile.absolutePath,
                "-x", "1",
                "-y", "1",
                "-t", "pixel"
        ], "")
        String expected = "184.0" + NEW_LINE
        assertEquals(expected, actual)

        actual = runApp([
                "raster get value",
                "-i", inFile.absolutePath,
                "-x", "1",
                "-y", "1",
                "-t", "point"
        ], "")
        expected = "227.0" + NEW_LINE
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster.asc")
        String actual = runApp([
                "raster get value",
                "-x", "1",
                "-y", "1",
                "-t", "pixel"
        ], reader.text)
        String expected = "184.0" + NEW_LINE
        assertEquals(expected, actual)

        reader = getStringReader("raster.asc")
        actual = runApp([
                "raster get value",
                "-x", "-174",
                "-y", "83",
                "-t", "point"
        ], reader.text)
        expected = "185.0" + NEW_LINE
        assertEquals(expected, actual)
    }
}
