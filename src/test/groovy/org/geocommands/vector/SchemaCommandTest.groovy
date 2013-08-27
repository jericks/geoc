package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.SchemaCommand.SchemaOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The SchemaCommand Unit Test
 * @author Jared Erickson
 */
class SchemaCommandTest extends BaseTest {

    @Test void execute() {
        SchemaCommand cmd = new SchemaCommand()
        File file1 = getResource("points.properties")

        // Regular
        SchemaOptions options = new SchemaOptions(
            inputWorkspace: file1.absolutePath,
            prettyPrint: false
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = """name,type
the_geom,Point
distance,String
name,String"""
        assertEquals(expected, actual)

        // Pretty print
        options = new SchemaOptions(
            inputWorkspace: file1.absolutePath,
            prettyPrint: true
        )
        writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        actual = writer.toString()
        expected = """---------------------
| name     | type   |
---------------------
| the_geom | Point  |
| distance | String |
| name     | String |
---------------------"""
        assertEquals(expected, actual)
    }

    @Test void executeWithCsv() {
        SchemaCommand cmd = new SchemaCommand()
        File file = getResource("polygons.properties")
        SchemaOptions options = new SchemaOptions(
            prettyPrint: false
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String actual = writer.toString()
        String expected = """name,type
the_geom,Point
distance,String
name,String"""
        assertEquals(expected, actual)
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.properties")
        String output = runApp([
            "vector schema",
            "-i", file.absolutePath,
        ],"")
        String actual = output
        String expected = """name,type
the_geom,Point
distance,String
name,String
"""
        assertEquals(expected, actual)

        output = runApp([
            "vector schema",
            "-p"
        ],readCsv("points.csv").text)
        actual = output
        expected = """---------------------
| name     | type   |
---------------------
| the_geom | Point  |
| distance | String |
| name     | String |
---------------------
"""
        assertEquals(expected, actual)

    }

}
