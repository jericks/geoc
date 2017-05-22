package org.geocommands.map

import org.geocommands.BaseTest
import org.geocommands.map.MapCubeCommand.MapCubeOptions
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * The MapCubeCommand Unit Test.
 * @author Jared Erickson
 */
class MapCubeCommandTest extends BaseTest {

    @Test
    void execute() {
        File rasterFile = getResource("raster.tif")
        File cubeFile = createTemporaryFile("cube", "png")
        MapCubeCommand cmd = new MapCubeCommand()
        MapCubeOptions options = new MapCubeOptions(
                layers: [
                        "layertype=raster source=${rasterFile.absolutePath}"
                ],
                file: cubeFile
        )
        cmd.execute(options)
        assertTrue cubeFile.exists()
        assertTrue cubeFile.length() > 0
    }

    @Test
    void executeBlank() {
        File cubeFile = createTemporaryFile("cube", "png")
        MapCubeCommand cmd = new MapCubeCommand()
        MapCubeOptions options = new MapCubeOptions(
                file: cubeFile,
                tabSize: 60,
                drawTabs: true
        )
        cmd.execute(options)
        assertTrue cubeFile.exists()
        assertTrue cubeFile.length() > 0
    }

    @Test
    void executeWithTitleAndSource() {
        File rasterFile = getResource("raster.tif")
        File cubeFile = createTemporaryFile("cube", "png")
        MapCubeCommand cmd = new MapCubeCommand()
        MapCubeOptions options = new MapCubeOptions(
                layers: [
                        "layertype=raster source=${rasterFile.absolutePath}"
                ],
                file: cubeFile,
                title: "Map Cube",
                source: "http://naturalearth.org"
        )
        cmd.execute(options)
        assertTrue cubeFile.exists()
        assertTrue cubeFile.length() > 0
    }

    @Test
    void run() {
        File rasterFile = getResource("raster.tif")
        File cubeFile = createTemporaryFile("cube", "png")
        runApp([
                "map cube",
                "-l", "layertype=raster source=${rasterFile.absolutePath}",
                "-f", cubeFile,
                "-o"
        ], "")
        assertTrue cubeFile.exists()
        assertTrue cubeFile.length() > 0
    }

    @Test
    void runBlank() {
        File cubeFile = createTemporaryFile("cube", "png")
        runApp([
                "map cube",
                "-f", cubeFile,
                "-t",
                "-s", "60"
        ], "")
        assertTrue cubeFile.exists()
        assertTrue cubeFile.length() > 0
    }

}
