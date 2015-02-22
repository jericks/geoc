package org.geocommands.raster

import geoscript.render.Map
import org.geocommands.BaseTest
import org.geocommands.raster.DisplayRasterCommand.DisplayRasterOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The DisplayRasterCommand Unit Test
 * @author Jared Erickson
 */
class DisplayRasterCommandTest extends BaseTest {

    @Test void execute() {
        DisplayRasterCommand cmd = new DisplayRasterCommand() {
            @Override
            protected void display(Map map, DisplayRasterOptions options) {
                assertNotNull map
                assertNotNull options
            }
        }
        DisplayRasterCommand.DisplayRasterOptions options = new DisplayRasterCommand.DisplayRasterOptions(
                inputRaster: getResource("raster.tif")
        )
        cmd.execute(options)
    }

    @Test void executeWithString() {
        DisplayRasterCommand cmd = new DisplayRasterCommand() {
            @Override
            protected void display(Map map, DisplayRasterOptions options) {
                assertNotNull map
                assertNotNull options
            }
        }
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        DisplayRasterCommand.DisplayRasterOptions options = new DisplayRasterCommand.DisplayRasterOptions()
        cmd.execute(options, reader, writer)
    }

    @Test void run() {
        DisplayRasterCommand.metaClass.display = { Map map, DisplayRasterCommand.DisplayRasterOptions options ->
            assertNotNull map
            assertNotNull options
        }
        runApp([
                "raster display",
                "-i", getResource("raster.tif").absolutePath
        ],"")
    }

    @Test void runWithStrings() {
        DisplayRasterCommand.metaClass.display = { Map map, DisplayRasterCommand.DisplayRasterOptions options ->
            assertNotNull map
            assertNotNull options
        }
        runApp([
                "raster display"
        ], getStringReader("raster.asc").text)
    }

}
