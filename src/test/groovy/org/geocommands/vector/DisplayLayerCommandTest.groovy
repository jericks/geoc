package org.geocommands.vector

import geoscript.render.Map
import org.geocommands.vector.DisplayLayerCommand.DisplayLayerOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The DisplayLayerCommand Unit Test
 * @author Jared Erickson
 */
class DisplayLayerCommandTest extends BaseTest {

    @Test void execute() {
        DisplayLayerCommand cmd = new DisplayLayerCommand() {
            @Override
            protected void display(Map map, DisplayLayerOptions options) {
                assertNotNull map
                assertNotNull options
            }
        }
        DisplayLayerOptions options = new DisplayLayerOptions(
                inputWorkspace: getResource("points.properties")
        )
        cmd.execute(options)
    }

    @Test void executeWithString() {
        DisplayLayerCommand cmd = new DisplayLayerCommand() {
            @Override
            protected void display(Map map, DisplayLayerOptions options) {
                assertNotNull map
                assertNotNull options
            }
        }
        StringReader reader = getStringReader("points.csv")
        StringWriter writer = new StringWriter()
        DisplayLayerOptions options = new DisplayLayerOptions()
        cmd.execute(options, reader, writer)
    }

    @Test void run() {
        DisplayLayerCommand.metaClass.display = { Map map, DisplayLayerOptions options ->
            assertNotNull map
            assertNotNull options
        }
        runApp([
                "vector display",
                "-i", getResource("points.properties").absolutePath
        ],"")
    }

    @Test void runWithStrings() {
        DisplayLayerCommand.metaClass.display = { Map map, DisplayLayerOptions options ->
            assertNotNull map
            assertNotNull options
        }
        runApp([
                "vector display"
        ], getStringReader("points.csv").text)
    }

}
