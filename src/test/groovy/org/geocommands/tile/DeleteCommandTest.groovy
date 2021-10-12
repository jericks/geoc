package org.geocommands.tile

import geoscript.layer.MBTiles
import geoscript.layer.Tile
import org.geocommands.tile.GenerateCommand.GenerateOptions
import org.geocommands.tile.DeleteCommand.DeleteOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The DeleteCommand Unit Test
 * @author Jared Erickson
 */
class DeleteCommandTest extends BaseTest {

    @Test void execute() {
        File mbtilesFile = generateMBTiles()
        DeleteCommand cmd = new DeleteCommand()
        DeleteOptions options = new DeleteOptions(
                tileLayer: mbtilesFile.absolutePath,
                z: 1
        )
        cmd.execute(options)

        MBTiles mbtiles = new MBTiles(mbtilesFile)
        (0..2).each { int z ->
            mbtiles.tiles(z).each{ Tile t ->
                if (z == 1) {
                    assertNull t.data
                } else {
                    assertNotNull t.data
                }
            }
        }
    }

    @Test void run() {
        File mbtilesFile = generateMBTiles()
        String result = runApp([
                "tile delete",
                "-l", mbtilesFile.absolutePath,
                "-i", "1/1/0"
        ],"")
        MBTiles mbtiles = new MBTiles(mbtilesFile)
        (0..2).each { int z ->
            mbtiles.tiles(z).each{ Tile t ->
                if (t.z == 1 && t.x == 1 && t.y == 0) {
                    assertNull t.data
                } else {
                    assertNotNull t.data
                }
            }
        }
    }

    private File generateMBTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = new File(folder, "earthquakes.mbtiles")
        tileFile.delete()
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: tileFile.absolutePath,
                layers: [
                  "layertype=layer file=${layerFile.absolutePath}"
                ],
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)
        tileFile
    }

}
