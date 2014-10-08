package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.workspace.Directory
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.SplitByLayerCommand.SplitByLayerOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The SplitByCommand Unit Test
 * @author Jared Erickson
 */
class SplitByLayerCommandTest extends BaseTest {

    @Test
    void execute() {
        File file1 = getResource("polygons2.properties")
        File file2 = getResource("polygons.properties")
        File file3 = folder.newFolder("rows")
        SplitByLayerCommand cmd = new SplitByLayerCommand()
        SplitByLayerOptions options = new SplitByLayerOptions(
                inputWorkspace: file2.absolutePath,
                splitWorkspace: file1.absolutePath,
                outputWorkspace: file3.absolutePath,
                field: "row_col"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Directory dir = new Directory(file3)
        assertEquals 2, dir.names.size()
        assertTrue dir.names.contains("polygons_1_1")
        assertTrue dir.names.contains("polygons_1_2")
        Layer layer11 = dir.get("polygons_1_1")
        assertEquals new Bounds(0.0, 0.0, 5.0, 10.0, "EPSG:4326"), layer11.bounds
        List features = layer11.features
        assertEquals 4, features.size()
        assertEquals "polygons_1_1.1 the_geom: MULTIPOLYGON (((0 0, 0 5, 5 5, 5 0, 0 0))), id: 0, row: 0, col: 0", features[0].toString()
        assertEquals "polygons_1_1.2 the_geom: MULTIPOLYGON (((5 0, 5 5, 5 0, 5 0))), id: 2, row: 0, col: 1", features[1].toString()
        assertEquals "polygons_1_1.3 the_geom: MULTIPOLYGON (((0 5, 0 10, 5 10, 5 5, 0 5))), id: 1, row: 1, col: 0", features[2].toString()
        assertEquals "polygons_1_1.4 the_geom: MULTIPOLYGON (((5 5, 5 10, 5 5, 5 5))), id: 3, row: 1, col: 1", features[3].toString()
        Layer layer12 = dir.get("polygons_1_2")
        assertEquals new Bounds(5.0, 0.0, 10.0, 10.0, "EPSG:4326"), layer12.bounds
        features = layer12.features
        assertEquals 4, features.size()
        assertEquals "polygons_1_2.1 the_geom: MULTIPOLYGON (((5 5, 5 0, 5 5, 5 5))), id: 0, row: 0, col: 0", features[0].toString()
        assertEquals "polygons_1_2.2 the_geom: MULTIPOLYGON (((5 0, 5 5, 10 5, 10 0, 5 0))), id: 2, row: 0, col: 1", features[1].toString()
        assertEquals "polygons_1_2.3 the_geom: MULTIPOLYGON (((5 10, 5 5, 5 10, 5 10))), id: 1, row: 1, col: 0", features[2].toString()
        assertEquals "polygons_1_2.4 the_geom: MULTIPOLYGON (((5 5, 5 10, 10 10, 10 5, 5 5))), id: 3, row: 1, col: 1", features[3].toString()
    }

    @Test
    void executeCsv() {
        File file1 = getResource("polygons2.properties")
        SplitByLayerCommand cmd = new SplitByLayerCommand()
        SplitByLayerOptions options = new SplitByLayerOptions(
                splitWorkspace: file1.absolutePath,
                field: "row_col"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        assertStringsEqual """csv_1_1
"the_geom:Polygon","id:String","row:String","col:String"
"POLYGON ((0 0, 0 5, 5 5, 5 0, 0 0))","0","0","0"
"POLYGON ((5 0, 5 5, 5 0, 5 0))","2","0","1"
"POLYGON ((0 5, 0 10, 5 10, 5 5, 0 5))","1","1","0"
"POLYGON ((5 5, 5 10, 5 5, 5 5))","3","1","1"

csv_1_2
"the_geom:Polygon","id:String","row:String","col:String"
"POLYGON ((5 5, 5 0, 5 5, 5 5))","0","0","0"
"POLYGON ((5 0, 5 5, 10 5, 10 0, 5 0))","2","0","1"
"POLYGON ((5 10, 5 5, 5 10, 5 10))","1","1","0"
"POLYGON ((5 5, 5 10, 10 10, 10 5, 5 5))","3","1","1"
""", w.toString()

    }

    @Test
    void runAsCommandLine() {
        File file1 = getResource("polygons2.properties")
        File file2 = getResource("polygons.properties")
        File file3 = folder.newFolder("rows")
        App.main([
                "vector splitbylayer",
                "-i", file2.absolutePath,
                "-s", file1.absolutePath,
                "-o", file3.absolutePath,
                "-f", "row_col"
        ] as String[])
        Directory dir = new Directory(file3)
        assertEquals 2, dir.names.size()
        assertTrue dir.names.contains("polygons_1_1")
        assertTrue dir.names.contains("polygons_1_2")
        Layer layer11 = dir.get("polygons_1_1")
        assertEquals new Bounds(0.0, 0.0, 5.0, 10.0, "EPSG:4326"), layer11.bounds
        List features = layer11.features
        assertEquals 4, features.size()
        assertEquals "polygons_1_1.1 the_geom: MULTIPOLYGON (((0 0, 0 5, 5 5, 5 0, 0 0))), id: 0, row: 0, col: 0", features[0].toString()
        assertEquals "polygons_1_1.2 the_geom: MULTIPOLYGON (((5 0, 5 5, 5 0, 5 0))), id: 2, row: 0, col: 1", features[1].toString()
        assertEquals "polygons_1_1.3 the_geom: MULTIPOLYGON (((0 5, 0 10, 5 10, 5 5, 0 5))), id: 1, row: 1, col: 0", features[2].toString()
        assertEquals "polygons_1_1.4 the_geom: MULTIPOLYGON (((5 5, 5 10, 5 5, 5 5))), id: 3, row: 1, col: 1", features[3].toString()
        Layer layer12 = dir.get("polygons_1_2")
        assertEquals new Bounds(5.0, 0.0, 10.0, 10.0, "EPSG:4326"), layer12.bounds
        features = layer12.features
        assertEquals 4, features.size()
        assertEquals "polygons_1_2.1 the_geom: MULTIPOLYGON (((5 5, 5 0, 5 5, 5 5))), id: 0, row: 0, col: 0", features[0].toString()
        assertEquals "polygons_1_2.2 the_geom: MULTIPOLYGON (((5 0, 5 5, 10 5, 10 0, 5 0))), id: 2, row: 0, col: 1", features[1].toString()
        assertEquals "polygons_1_2.3 the_geom: MULTIPOLYGON (((5 10, 5 5, 5 10, 5 10))), id: 1, row: 1, col: 0", features[2].toString()
        assertEquals "polygons_1_2.4 the_geom: MULTIPOLYGON (((5 5, 5 10, 10 10, 10 5, 5 5))), id: 3, row: 1, col: 1", features[3].toString()

        String actual = runApp(["vector splitbylayer", "-s", file1.absolutePath, "-f", "row_col"], readCsv("polygons.csv").text)
        String expected = """csv_1_1
"the_geom:Polygon","id:String","row:String","col:String"
"POLYGON ((0 0, 0 5, 5 5, 5 0, 0 0))","0","0","0"
"POLYGON ((5 0, 5 5, 5 0, 5 0))","2","0","1"
"POLYGON ((0 5, 0 10, 5 10, 5 5, 0 5))","1","1","0"
"POLYGON ((5 5, 5 10, 5 5, 5 5))","3","1","1"

csv_1_2
"the_geom:Polygon","id:String","row:String","col:String"
"POLYGON ((5 5, 5 0, 5 5, 5 5))","0","0","0"
"POLYGON ((5 0, 5 5, 10 5, 10 0, 5 0))","2","0","1"
"POLYGON ((5 10, 5 5, 5 10, 5 10))","1","1","0"
"POLYGON ((5 5, 5 10, 10 10, 10 5, 5 5))","3","1","1"

"""
        assertStringsEqual expected, actual
    }

}
