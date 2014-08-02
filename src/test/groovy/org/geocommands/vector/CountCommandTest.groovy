package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.workspace.Property
import org.geocommands.BaseTest
import org.geocommands.vector.CountCommand.CountOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The CountCommand UnitTest
 * @author Jared Erickson
 */
class CountCommandTest extends BaseTest {

    @Test
    void execute() {
        CountCommand cmd = new CountCommand()
        File file = getResource("points.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        CountOptions options = new CountOptions(
                inputWorkspace: file.absolutePath
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals 3, w.toString() as Integer
    }

    @Test
    void executeCountGeometries() {
        CountCommand cmd = new CountCommand()
        File file = getResource("polygons.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        CountOptions options = new CountOptions(
                inputWorkspace: file.absolutePath,
                type: "geometries"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals 4, w.toString() as Integer
    }

    @Test
    void executeCountPoints() {
        CountCommand cmd = new CountCommand()
        File file = getResource("polygons.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        CountOptions options = new CountOptions(
                inputWorkspace: file.absolutePath,
                type: "points"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals 20, w.toString() as Integer
    }

    @Test
    void executeWithCsv() {
        CountCommand cmd = new CountCommand()
        CountOptions options = new CountOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        assertEquals 3, w.toString() as Integer
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        String output = runApp(["vector count",
                                "-i", file.absolutePath
        ], "")
        assertEquals 3, output as Integer

        output = runApp(["vector count"], readCsv("points.csv").text)
        assertEquals 3, output as Integer
    }

}
