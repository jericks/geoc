package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.CompareSchemasCommand.CompareSchemasOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The CompareSchemaCommand Unit Test
 * @author Jared Erickson
 */
class CompareSchemasCommandTest extends BaseTest {

    @Test
    void execute() {
        CompareSchemasCommand cmd = new CompareSchemasCommand()
        File file1 = getResource("points.properties")
        File file2 = getResource("polygons.properties")

        // Regular
        CompareSchemasOptions options = new CompareSchemasOptions(
                inputWorkspace: file1.absolutePath,
                otherWorkspace: file2.absolutePath,
                prettyPrint: false
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = """Name,Type,points,polygons
the_geom,Point,true,false
distance,String,true,false
name,String,true,false
the_geom,Polygon,false,true
id,Integer,false,true
row,Integer,false,true
col,Integer,false,true"""
        assertStringsEqual(expected, actual)

        // Pretty print
        options = new CompareSchemasOptions(
                inputWorkspace: file1.absolutePath,
                otherWorkspace: file2.absolutePath,
                prettyPrint: true
        )
        writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        actual = writer.toString()
        expected = """------------------------------------------
| Name     | Type    | points | polygons |
------------------------------------------
| the_geom | Point   | true   | false    |
| distance | String  | true   | false    |
| name     | String  | true   | false    |
| the_geom | Polygon | false  | true     |
| id       | Integer | false  | true     |
| row      | Integer | false  | true     |
| col      | Integer | false  | true     |
------------------------------------------"""
        assertStringsEqual(expected, actual)
    }

    @Test
    void executeWithCsv() {
        CompareSchemasCommand cmd = new CompareSchemasCommand()
        File file = getResource("polygons.properties")
        CompareSchemasOptions options = new CompareSchemasOptions(
                otherWorkspace: file.absolutePath,
                prettyPrint: false
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String actual = writer.toString()
        String expected = """Name,Type,csv,polygons
the_geom,Point,true,false
distance,String,true,false
name,String,true,false
the_geom,Polygon,false,true
id,Integer,false,true
row,Integer,false,true
col,Integer,false,true"""
        assertStringsEqual(expected, actual)
    }

    @Test
    void runAsCommandLine() {
        File file1 = getResource("points.properties")
        File file2 = getResource("polygons.properties")
        String output = runApp([
                "vector compareschemas",
                "-i", file1.absolutePath,
                "-k", file2.absolutePath
        ], "")
        String actual = output
        String expected = """Name,Type,points,polygons
the_geom,Point,true,false
distance,String,true,false
name,String,true,false
the_geom,Polygon,false,true
id,Integer,false,true
row,Integer,false,true
col,Integer,false,true
"""
        assertStringsEqual(expected, actual)

        output = runApp([
                "vector compareschemas",
                "-k", file2.absolutePath,
                "-p"
        ], readCsv("points.csv").text)
        actual = output
        expected = """-----------------------------------------
| Name     | Type    | csv   | polygons |
-----------------------------------------
| the_geom | Point   | true  | false    |
| distance | String  | true  | false    |
| name     | String  | true  | false    |
| the_geom | Polygon | false | true     |
| id       | Integer | false | true     |
| row      | Integer | false | true     |
| col      | Integer | false | true     |
-----------------------------------------
"""
        assertStringsEqual(expected, actual)

    }

}
