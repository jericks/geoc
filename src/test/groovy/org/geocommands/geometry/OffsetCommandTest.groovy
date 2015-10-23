package org.geocommands.geometry

import org.geocommands.geometry.OffsetCommand.OffsetOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals

class OffsetCommandTest extends BaseTest {

    @Test void execute() {
        OffsetCommand cmd = new OffsetCommand()
        OffsetOptions options = new OffsetOptions(
                offset: 5,
                quadrantSegments: 8
        )
        StringReader reader = new StringReader("LINESTRING (5 10, 10 10)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("LINESTRING (5 15, 10 15)", writer.toString())

        options = new OffsetOptions(
                input: "LINESTRING (5 10, 10 10)",
                offset: 5,
                quadrantSegments: 8
        )
        String result = cmd.execute(options)
        assertEquals("LINESTRING (5 15, 10 15)", result)
    }

    @Test void run() {
        String result = runApp([
                "geometry offset",
                "-i", "LINESTRING (5 10, 10 10)",
                "-d", "5",
                "-s", "8"
        ],"")
        assertEquals("LINESTRING (5 15, 10 15)" + NEW_LINE, result)

        result = runApp([
                "geometry offset",
                "-d", "5",
                "-s", "8"
        ],"LINESTRING (5 10, 10 10)")
        assertEquals("LINESTRING (5 15, 10 15)" + NEW_LINE, result)
    }
}
