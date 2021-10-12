package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.GeometryWriterCommand.GeometryWriterOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The GeometryWriterCommand Unit Test
 * @author Jared Erickson
 */
class GeometryWriterCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points.properties")
        GeometryWriterCommand cmd = new GeometryWriterCommand()
        GeometryWriterOptions options = new GeometryWriterOptions(
                inputWorkspace: file.absolutePath
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = """POINT (1 1)
POINT (10 10)
POINT (2 8)"""
        assertStringsEqual actual, expected
    }

    @Test
    void executeCsv() {
        GeometryWriterCommand cmd = new GeometryWriterCommand()
        GeometryWriterOptions options = new GeometryWriterOptions()
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String actual = writer.toString()
        String expected = """POINT (1 1)
POINT (10 10)
POINT (2 8)"""
        assertStringsEqual actual, expected
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        String actual = runApp(["vector geomw", "-i", file.absolutePath], "")
        String expected = """POINT (1 1)
POINT (10 10)
POINT (2 8)
"""
        assertStringsEqual actual, expected

        actual = runApp(["vector geomw"], readCsv("points.csv").text)
        expected = """POINT (1 1)
POINT (10 10)
POINT (2 8)
"""
        assertStringsEqual actual, expected
    }
}
