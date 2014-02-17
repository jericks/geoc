package org.geocommands.geometry

import geoscript.geom.Point
import org.geocommands.BaseTest
import org.geocommands.geometry.PlotCommand.PlotOptions
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by jericks on 2/17/14.
 */
class PlotCommandTest extends BaseTest {

    @Test
    void execute() {
        PlotCommand cmd = new PlotCommand()
        File file = File.createTempFile("plot", ".png")
        PlotOptions options = new PlotOptions(
                input: new Point(100, 100).buffer(20).wkt,
                file: file.absolutePath
        )
        cmd.execute(options)
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    void run() {
        File file = File.createTempFile("plot", ".png")
        runApp([
                "geometry plot",
                "-f", file.absolutePath,
                "-d"
        ], new Point(100, 100).buffer(20).wkt)
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

}
