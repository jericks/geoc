package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Cursor
import geoscript.layer.Layer
import geoscript.workspace.Directory
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.SplitByFieldCommand.SplitByFieldOptions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

/**
 * The SplitByFieldCommand Unit Test
 * @author Jared Erickson
 */
class SplitByFieldCommandTest extends BaseTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test void execute() {
        SplitByFieldCommand cmd = new SplitByFieldCommand()
        File file = getResource("polygons.properties")
        File directory = folder.newFolder("rows")
        SplitByFieldOptions options = new SplitByFieldOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: directory.absolutePath,
            field: "row"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Directory dir = new Directory(directory)
        assertEquals 2, dir.names.size()
        assertTrue dir.names.contains("polygons_row_0")
        assertTrue dir.names.contains("polygons_row_1")

        Layer layer = dir.get("polygons_row_0")
        Cursor c = layer.cursor
        Feature f = c.next()
        assertEquals 0, f['id']
        assertEquals 0, f['row']
        assertEquals 0, f['col']
        assertEquals "MULTIPOLYGON (((0 0, 0 5, 5 5, 5 0, 0 0)))", f.geom.wkt
        f = c.next()
        assertEquals 2, f['id']
        assertEquals 0, f['row']
        assertEquals 1, f['col']
        assertEquals "MULTIPOLYGON (((5 0, 5 5, 10 5, 10 0, 5 0)))", f.geom.wkt

        layer = dir.get("polygons_row_1")
        c = layer.cursor
        f = c.next()
        assertEquals 1, f['id']
        assertEquals 1, f['row']
        assertEquals 0, f['col']
        assertEquals "MULTIPOLYGON (((0 5, 0 10, 5 10, 5 5, 0 5)))", f.geom.wkt
        f = c.next()
        assertEquals 3, f['id']
        assertEquals 1, f['row']
        assertEquals 1, f['col']
        assertEquals "MULTIPOLYGON (((5 5, 5 10, 10 10, 10 5, 5 5)))", f.geom.wkt
    }

    @Test void executeCsv() {
        SplitByFieldCommand cmd = new SplitByFieldCommand()
        SplitByFieldOptions options = new SplitByFieldOptions(
            field: "row"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        String actual = w.toString()
        String expected = """csv_row_1
"the_geom","id","row","col"
"POLYGON ((0 5, 0 10, 5 10, 5 5, 0 5))","1","1","0"
"POLYGON ((5 5, 5 10, 10 10, 10 5, 5 5))","3","1","1"

csv_row_0
"the_geom","id","row","col"
"POLYGON ((0 0, 0 5, 5 5, 5 0, 0 0))","0","0","0"
"POLYGON ((5 0, 5 5, 10 5, 10 0, 5 0))","2","0","1"
"""
        assertEquals expected, actual
    }

    @Test void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File directory = folder.newFolder("rows")
        App.main([
                "vector splitbyfield",
                "-i", file.absolutePath,
                "-o", directory.absolutePath,
                "-f", "row"
        ] as String[])
        Directory dir = new Directory(directory)
        assertEquals 2, dir.names.size()
        assertTrue dir.names.contains("polygons_row_0")
        assertTrue dir.names.contains("polygons_row_1")

        Layer layer = dir.get("polygons_row_0")
        Cursor c = layer.cursor
        Feature f = c.next()
        assertEquals 0, f['id']
        assertEquals 0, f['row']
        assertEquals 0, f['col']
        assertEquals "MULTIPOLYGON (((0 0, 0 5, 5 5, 5 0, 0 0)))", f.geom.wkt
        f = c.next()
        assertEquals 2, f['id']
        assertEquals 0, f['row']
        assertEquals 1, f['col']
        assertEquals "MULTIPOLYGON (((5 0, 5 5, 10 5, 10 0, 5 0)))", f.geom.wkt

        layer = dir.get("polygons_row_1")
        c = layer.cursor
        f = c.next()
        assertEquals 1, f['id']
        assertEquals 1, f['row']
        assertEquals 0, f['col']
        assertEquals "MULTIPOLYGON (((0 5, 0 10, 5 10, 5 5, 0 5)))", f.geom.wkt
        f = c.next()
        assertEquals 3, f['id']
        assertEquals 1, f['row']
        assertEquals 1, f['col']
        assertEquals "MULTIPOLYGON (((5 5, 5 10, 10 10, 10 5, 5 5)))", f.geom.wkt

        String actual = runApp(["vector splitbyfield", "-f", "row"], readCsv("polygons.csv").text)
        String expected = """csv_row_1
"the_geom","id","row","col"
"POLYGON ((0 5, 0 10, 5 10, 5 5, 0 5))","1","1","0"
"POLYGON ((5 5, 5 10, 10 10, 10 5, 5 5))","3","1","1"

csv_row_0
"the_geom","id","row","col"
"POLYGON ((0 0, 0 5, 5 5, 5 0, 0 0))","0","0","0"
"POLYGON ((5 0, 5 5, 10 5, 10 0, 5 0))","2","0","1"

"""
        assertEquals expected, actual
    }
}
