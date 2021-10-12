package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.workspace.Directory
import org.geocommands.vector.DumpShapefilesCommand.DumpShapefilesOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

/**
 * The DumpShapefilesCommand Unit Test
 * @author Jared Erickson
 */
class DumpShapefilesCommandTest extends BaseTest {

    @Test void execute() {
        File dir = createDir("shapefiles")
        DumpShapefilesOptions options = new DumpShapefilesOptions(
                outputDirectory: dir
        )
        DumpShapefilesCommand cmd = new DumpShapefilesCommand()
        cmd.execute(options, getStringReader("pointsAndPolygons.csv"), new StringWriter())
        Directory workspace = new Directory(dir)
        assertTrue workspace.names.size() == 2
        Layer pointLayer = workspace.layers.find { it.name.endsWith("Point") }
        assertEquals 10, pointLayer.count
        Layer polygonLayer = workspace.layers.find { it.name.endsWith("Polygon") }
        assertEquals 10, polygonLayer.count
    }

    @Test void run() {
        File dir = createDir("shapefiles")
        runApp([
                "vector dump shapefiles",
                "-o", dir
        ], getStringReader("pointsAndPolygons.csv").text)
        Directory workspace = new Directory(dir)
        println workspace.names
        assertTrue workspace.names.size() == 2
        Layer pointLayer = workspace.layers.find { it.name.endsWith("Point") }
        assertEquals 10, pointLayer.count
        Layer polygonLayer = workspace.layers.find { it.name.endsWith("Polygon") }
        assertEquals 10, polygonLayer.count
    }

}
