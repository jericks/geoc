package org.geocommands.tile

import org.geocommands.tile.PyramidCommand.PyramidOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The PyramidCommand Unit Test
 * @author Jared Erickson
 */
class PyramidCommandTest extends BaseTest {

    @Test void executeText() {
        PyramidCommand cmd = new PyramidCommand()
        PyramidOptions options  = new PyramidOptions(
            tileLayer: getResource("earthquakes.mbtiles").absolutePath,
            outputType: "text"
        )
        String result = cmd.execute(options)
        assertTrue result.startsWith('EPSG:3857')
        assertTrue result.contains('0,1,1,156412.0,156412.0')
    }

    @Test void executeJson() {
        PyramidCommand cmd = new PyramidCommand()
        PyramidOptions options  = new PyramidOptions(
                tileLayer: getResource("earthquakes.mbtiles").absolutePath,
                outputType: "json"
        )
        String result = cmd.execute(options)
        println result
        assertTrue result.startsWith('{')
        assertTrue result.contains('"proj": "EPSG:3857"')
        assertTrue result.contains('"origin": "BOTTOM_LEFT"')
    }

    @Test void executeXml() {
        PyramidCommand cmd = new PyramidCommand()
        PyramidOptions options  = new PyramidOptions(
                tileLayer: getResource("earthquakes.mbtiles").absolutePath,
                outputType: "xml"
        )
        String result = cmd.execute(options)
        println result
        assertTrue result.startsWith('<pyramid>')
        assertTrue result.contains('<proj>EPSG:3857</proj>')
    }

    @Test void run() {
        String result = runApp([
                "tile pyramid",
                "-l", getResource("earthquakes.mbtiles").absolutePath,
                "-o", "csv"
        ], "")
        assertTrue result.startsWith('EPSG:3857')
        assertTrue result.contains('0,1,1,156412.0,156412.0')
    }

}
