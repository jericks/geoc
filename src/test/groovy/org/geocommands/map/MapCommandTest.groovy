package org.geocommands.map

import org.geocommands.BaseTest
import org.geocommands.map.MapCommand.MapOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The MapCommand Unit Test.
 * @author Jared Erickson
 */
class MapCommandTest extends BaseTest {

    private File getFile(String name) {
        folder.newFile(name)
    }

    @Test void execute() {
        MapCommand cmd = new MapCommand()
        File outFile = getFile("image1.png")
        MapOptions options = new MapOptions(
            layers: [
                    "layertype=layer directory=${getResource('polygons.properties')} layername=polygons",
                    "layertype=layer directory=${getResource('points.properties')} layername=points"
            ],
            file: outFile,
            backgroundColor: "white"
        )
        println cmd.execute(options)
        assertTrue outFile.exists()
        assertTrue outFile.length() > 0
    }

    @Test void run() {
        File outFile = getFile("image2.png")
        println runApp([
                "map draw",
                "-l", "layertype=layer file=${getResource('polygons.properties')} layername=polygons",
                "-l", "layertype=layer file=${getResource('points.properties')} layername=points",
                '-w', 400,
                '-h', 400,
                '-g', 'black',
                '-f', outFile.absolutePath
        ], "")
        assertTrue outFile.exists()
        assertTrue outFile.length() > 0
    }
}
