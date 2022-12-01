package org.geocommands.doc

import org.junit.jupiter.api.Test

class MapTest extends DocTest {

    @Test
    void map() {
        List commands = ["map", "draw",
             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
             "-f", "target/map.png"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_map_draw_command", command)
        copyFile(new File("target/map.png"), new File("src/main/docs/images/geoc_map_draw_command.png"))
    }

    @Test
    void mapCube() {
        List commands = ["map", "cube",
             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
             "-o",
             "-f", "target/cube.png"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_map_cube_command", command)
        copyFile(new File("target/cube.png"), new File("src/main/docs/images/geoc_map_cube_command.png"))
    }

    @Test
    void mapCubeBlank() {
        String command = "geoc map cube -o -f target/cube_blank.png"
        String result = runApp(command, "")
        writeTextFile("geoc_map_cube_blank_command", command)
        copyFile(new File("target/cube_blank.png"), new File("src/main/docs/images/geoc_map_cube_blank_command.png"))
    }

}
