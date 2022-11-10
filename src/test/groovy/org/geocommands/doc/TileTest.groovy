package org.geocommands.doc

import geoscript.layer.GeoPackage
import geoscript.layer.Layer
import geoscript.layer.MBTiles
import geoscript.layer.Pyramid
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.layer.TMS
import geoscript.layer.TileLayer
import geoscript.style.Stroke
import geoscript.workspace.Workspace
import org.junit.jupiter.api.Test

import javax.imageio.ImageIO

class TileTest extends DocTest {

    @Test
    void deleteTilesByZoomLevel() {
        runApp(["tile", "generate",
                         "-l", "type=mbtiles file=target/world.mbtiles",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
                         "-s", "0",
                         "-e", "2",
                         "--verbose"
        ], "")

        List commands = ["tile", "delete", "-l", "type=mbtiles file=target/world.mbtiles", "-z", "2"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") || it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_delete_tiles_zoom_command", command)
        writeTextFile("geoc_tile_delete_tiles_zoom_command_output", result)
    }

    @Test
    void generateMBTiles() {
        List commands = ["tile", "generate",
            "-l", "type=mbtiles file=target/world.mbtiles",
            "-m", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
            "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
            "-s", "0",
            "-e", "2",
            "--verbose"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("type") || it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_generate_mbtiles_command", command)
        writeTextFile("geoc_tile_generate_mbtiles_command_output", result)

        TileLayer tiles = new MBTiles(new File("target/world.mbtiles"))
        Raster raster = tiles.getRaster(tiles.tiles(1))
        ImageIO.write(raster.bufferedImage, "png", new File("src/main/docs/images/geoc_tile_generate_mbtiles_command.png"))
    }

    @Test
    void generateGeoPackage() {
        File file = new File("target/world.gpkg")
        if (file.exists()) {
            file.delete()
        }
        List commands = ["tile", "generate",
                         "-l", "type=geopackage file=target/world.gpkg name=world pyramid=geodetic",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
                         "-s", "0",
                         "-e", "2",
                         "--verbose"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("type") || it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_generate_geopackage_command", command)
        writeTextFile("geoc_tile_generate_geopackage_command_output", result)

        TileLayer tiles = new GeoPackage(new File("target/world.gpkg"), "world")
        Raster raster = tiles.getRaster(tiles.tiles(1))
        ImageIO.write(raster.bufferedImage, "png", new File("src/main/docs/images/geoc_tile_generate_geopackage_command.png"))
    }

    @Test
    void generateTMS() {
        List commands = ["tile", "generate",
                         "-l", "type=tms file=target/tiles",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
                         "-s", "0",
                         "-e", "2",
                         "--verbose"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("type") || it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_generate_tms_command", command)
        writeTextFile("geoc_tile_generate_tms_command_output", result)

        TileLayer tiles = new TMS("world", "png", new File("target/tiles"), Pyramid.createGlobalMercatorPyramid())
        Raster raster = tiles.getRaster(tiles.tiles(1))
        ImageIO.write(raster.bufferedImage, "png", new File("src/main/docs/images/geoc_tile_generate_tms_command.png"))
    }

    @Test
    void generatePbfVectorTiles() {
        File dir = new File("target/vectortiles")
        if(!dir.exists()) {
            dir.mkdirs()
        }
        List commands = ["tile", "generate",
                         "-l", "type=vectortiles format=pbf file=target/vectortiles",
                         "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries",
                         "-d", "countries=NAME,TYPE,LEVEL",
                         "-s", "0",
                         "-e", "2",
                         "--verbose"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("type") || it.startsWith("layertype") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_generate_pbf_command", command)
        writeTextFile("geoc_tile_generate_pbf_command_output", result)
    }

    @Test
    void tileBounds() {
        String command = "geoc tile get bounds -p mercator -z 3 -x 2 -y 1"
        String result = runApp(command, "")
        writeTextFile("geoc_tile_get_bounds_command", command)
        writeTextFile("geoc_tile_get_bounds_command_output", result)
    }

    @Test
    void listTiles() {
        String command = "geoc tile list tiles -p mercator -z 10 -b 2315277.538707974,4356146.199006655,2534193.2172859586,4470343.227121928"
        String result = runApp(command, "")
        writeTextFile("geoc_tile_list_tiles_command", command)
        writeTextFile("geoc_tile_list_tiles_command_output", result)
    }

    @Test
    void getPyramidText() {
        List commands = ["tile", "pyramid", "-l", "type=geopackage file=src/test/resources/data.gpkg name=world", "-o", "text"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_pyramid_text_command", command)
        writeTextFile("geoc_tile_pyramid_text_command_output", result)
    }

    @Test
    void getPyramidJson() {
        List commands = ["tile", "pyramid", "-l", "type=geopackage file=src/test/resources/data.gpkg name=world", "-o", "json"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_pyramid_json_command", command)
        writeTextFile("geoc_tile_pyramid_json_command_output", result)
    }

    @Test
    void getPyramidXml() {
        List commands = ["tile", "pyramid", "-l", "type=geopackage file=src/test/resources/data.gpkg name=world", "-o", "xml"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_pyramid_xml_command", command)
        writeTextFile("geoc_tile_pyramid_xml_command_output", result)
    }

    @Test
    void stitchRasterZoomLevel() {
        List commands = ["tile", "stitch", "raster", "-l", "type=geopackage file=src/test/resources/data.gpkg name=world", "-o", "target/world_1.png", "-z", "1"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_tile_stitch_raster_zoom_command", command)
        copyFile(new File("target/world_1.png"), new File("src/main/docs/images/geoc_tile_stitch_raster_zoom_command.png"))
    }

    @Test
    void stitchVectorZoomLevel() {

        File dir = new File("target/vectortiles")
        if(!dir.exists()) {
            dir.mkdirs()
        }

        File file = new File("target/world.gpkg")
        if (file.exists()) {
            file.delete()
        }

        String result = runApp([
            "tile", "generate",
            "-l", "type=vectortiles format=pbf file=target/vectortiles",
            "-m", "layertype=layer file=src/test/resources/data.gpkg layername=countries",
            "-d", "countries=NAME,TYPE,LEVEL",
            "-s", "0",
            "-e", "2",
            "--verbose"
        ], "")

        List commands = ["tile", "stitch", "vector", "-l", "type=vectortiles format=pbf file=target/vectortiles",
                    "-o", "type=geopackage file=target/world.gpkg name=world", "-z", "1"]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        result = runApp(commands, "")
        writeTextFile("geoc_tile_stitch_vector_zoom_command", command)

        Workspace workspace = new geoscript.workspace.GeoPackage(new File("target/world.gpkg"))
        writeTextFile("geoc_tile_stitch_vector_zoom_command_layers", workspace.names.join(", "))
        writeTextFile("geoc_tile_stitch_vector_zoom_command_schema", createSchemaTable(workspace["countries"].schema))
    }

    @Test
    void vectorGrid() {
        List commands = ["tile", "vector", "grid",
                         "-l", "type=geopackage file=src/main/resources/data.gpkg name=world",
                         "-o", "target/world_grid_1.shp",
                         "-z", "2"
        ]
        String command = "geoc " + commands.collect{
            it.startsWith("type") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        println result
        writeTextFile("geoc_tile_vector_grid_command", command)
        Layer layer = new Shapefile("target/world_grid_1.shp")
        layer.style = new Stroke("black", 1)
        drawOnBasemap("geoc_tile_vector_grid_command", [layer], proj: "EPSG:3857")
    }

}
