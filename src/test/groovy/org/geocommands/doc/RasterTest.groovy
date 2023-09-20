package org.geocommands.doc

import geoscript.geom.Bounds
import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.style.ColorMap
import geoscript.style.Label
import geoscript.style.Stroke
import geoscript.style.Style
import geoscript.style.io.SimpleStyleReader
import org.junit.jupiter.api.Test

class RasterTest extends DocTest {

    @Test
    void absolute() {
        String command = "geoc raster abs -i src/test/resources/absolute.tif -o target/absolute_abs.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_abs_command", command)
        writeTextFile("geoc_raster_abs_command_output", result)

        Style rasterStyle = new ColorMap(-10, 10, "Reds", 10)
        Style vectorStyle = new Stroke("black",1) + new Label("value")

        Closure createLayer = { Raster raster, Style style ->
            Layer layer = raster.polygonLayer
            layer.style = style
            layer
        }

        Raster raster = Format.getFormat(new File("src/test/resources/absolute.tif")).read("absolute")
        Raster absolute = Format.getFormat(new File("target/absolute_abs.tif")).read("absolute_abs")
        raster.style = rasterStyle
        absolute.style = rasterStyle
        draw("raster_absolute_1", [raster, createLayer(raster, vectorStyle)])
        draw("raster_absolute_2", [absolute, createLayer(absolute, vectorStyle)])
    }

    @Test
    void addConstant() {
        String getValue1Command = "geoc raster get value -i src/test/resources/pc.tif -x -121.799927 -y 46.867703"
        String getValue1Result = runApp(getValue1Command)
        writeTextFile("geoc_raster_add_constant_value1_command", getValue1Command)
        writeTextFile("geoc_raster_add_constant_value1_result", getValue1Result)

        String command = "geoc raster add constant -i src/test/resources/pc.tif -v 100 -o target/pc_add.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_add_constant_command", command)
        writeTextFile("geoc_raster_add_constant_command_output", result)

        Raster raster = Format.getFormat(new File("target/pc_add.tif")).read()
        raster.style = new ColorMap([
                [color: "#9fd182", quantity:25],
                [color: "#3e7f3c", quantity:470],
                [color: "#133912", quantity:920],
                [color: "#08306b", quantity:1370],
                [color: "#fffff5", quantity:1820],
        ])
        draw("geoc_raster_add_constant_command_raster", [raster])

        String getValue2Command = "geoc raster get value -i target/pc_add.tif -x -121.799927 -y 46.867703"
        String getValue2Result = runApp(getValue2Command)
        writeTextFile("geoc_raster_add_constant_value2_command", getValue2Command)
        writeTextFile("geoc_raster_add_constant_value2_result", getValue2Result)
    }

    @Test
    void add() {
        String command = "geoc raster add -i src/test/resources/low.tif -k src/test/resources/high.tif -o target/lowPlusHigh.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_add_command", command)
        writeTextFile("geoc_raster_add_command_output", result)

        Style rasterStyle = new ColorMap(1, 50, "MutedTerrain", 20)
        Style vectorStyle = new Stroke("black",1) + new Label("value")

        Raster lowRaster = Format.getFormat(new File("src/test/resources/low.tif")).read()
        Raster highRaster = Format.getFormat(new File("src/test/resources/high.tif")).read()
        Raster lowPlusHighRaster = Format.getFormat(new File("target/lowPlusHigh.tif")).read()

        lowRaster.style = rasterStyle
        highRaster.style = rasterStyle
        lowPlusHighRaster.style = rasterStyle

        Closure createLayer = { Raster raster, Style style ->
            Layer layer = raster.polygonLayer
            layer.style = style
            layer
        }

        draw("geoc_raster_add_command_low",  [lowRaster, createLayer(lowRaster, vectorStyle)])
        draw("geoc_raster_add_command_high", [highRaster, createLayer(highRaster, vectorStyle)])
        draw("geoc_raster_add_command_add",  [lowPlusHighRaster, createLayer(lowPlusHighRaster, vectorStyle)])
    }

    @Test
    void animatedGif() {
        List states = [
            [name: "Washington", bounds: new Bounds(-124.68721008300781,45.59199778907822,-116.90652787968992,49.000885321643864,"EPSG:4326")],
            [name: "Oregon", bounds: new Bounds(-124.53283999999996,41.99260508886846,-116.45779557988342,46.2830694871044,"EPSG:4326")],
            [name: "California", bounds: new Bounds(-124.39795772362243,32.535327053348965,-114.16597164595498,41.99947805436335,"EPSG:4326")]
        ]
        states.each { Map state ->
            String name = state.name
            Bounds bounds = state.bounds
            List commands = ["map", "draw",
                             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=ocean style=src/test/resources/ocean.sld",
                             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=countries style=src/test/resources/countries.sld",
                             "-l", "layertype=layer file=src/test/resources/data.gpkg layername=states style=src/test/resources/states.sld",
                             "-b", "${bounds.minX},${bounds.minY},${bounds.maxX},${bounds.maxY}",
                             "-f", "target/state_${name.toLowerCase()}.png"
            ]
            String command = "geoc " + commands.collect{
                it.startsWith("layertype") ? '"' + it + '"' : it
            }.join(" ")
            String result = runApp(commands, "")
            writeTextFile("geoc_animatedgif_${name.toLowerCase()}_command", command)
            copyFile(new File("target/state_${name.toLowerCase()}.png"), new File("src/main/docs/images/geoc_animatedgif_${name.toLowerCase()}.png"))
        }

        String command = "geoc raster animatedgif -f target/state_washington.png -f target/state_oregon.png -f target/state_california.png -o target/states.gif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_animatedgif_command", command)
        writeTextFile("geoc_raster_animatedgif_command_output", result)
        copyFile(new File("target/states.gif"), new File("src/main/docs/images/geoc_animatedgif.gif"))
    }

    @Test
    void cropWithBounds() {
        String command = "geoc raster crop -i src/test/resources/earth.tif -b -160.927734,6.751896,-34.716797,57.279043 -o target/earth_cropped.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_crop_command", command)
        writeTextFile("geoc_raster_crop_command_output", result)

        Raster raster = new GeoTIFF(new File("target/earth_cropped.tif")).read()
        drawOnBasemap("geoc_raster_crop_command", [raster])
    }

    @Test
    void cropWithGeometry() {
        List commands = [
                "raster",
                "crop",
                "with",
                "geometry",
                "-i",
                "src/test/resources/earth.tif",
                "-g",
                "POLYGON ((-120.06886118446164 54.657570186377484, -131.4744345802818 40.88641840854305, -120.66873293244274 27.841500134049014, -91.23852896646747 22.376168381822453, -75.66538001484537 23.99772020337508, -54.66444615739175 45.994788780815526, -91.94198075352523 53.20175611636799, -120.06886118446164 54.657570186377484))",
                "-o",
                "target/earth_cropped.tif"
            ]
        String command = "geoc " + commands.collect{
            it.startsWith("POLYGON") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_raster_crop_with_geometry_command", command)
        writeTextFile("geoc_raster_crop_with_geometry_command_output", result)

        Raster raster = new GeoTIFF(new File("target/earth_cropped.tif")).read()
        drawOnBasemap("geoc_raster_crop_with_geometry_command", [raster])
    }

    @Test
    void cropWithLayer() {

        // Random Points
        String randomCommand = "geoc vector randompoints -n 10 -g -180,-90,180,90 -o target/locations.shp"
        println runApp(randomCommand, "")
        writeTextFile("geoc_raster_crop_with_layer_random_points_command", randomCommand)

        // Buffer Points
        String bufferCommand = "geoc vector buffer -d 10 -i target/locations.shp -o target/buffers.shp"
        println runApp(bufferCommand, "")
        writeTextFile("geoc_raster_crop_with_layer_buffer_command", bufferCommand)

        // Crop
        String command = "geoc raster crop with layer -i src/test/resources/earth.tif -o target/earth_cropped.tif -w target/buffers.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_crop_with_layer_command", command)
        writeTextFile("geoc_raster_crop_with_layer_command_output", result)

        // Create Map
        Raster raster = new GeoTIFF(new File("target/earth_cropped.tif")).read()
        Layer pointsLayer = new Shapefile("target/locations.shp")
        pointsLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=6 shape=red")
        Layer bufferLayer = new Shapefile("target/buffers.shp")
        bufferLayer.style = new SimpleStyleReader().read("stroke=red stroke-width=2.5")
        drawOnBasemap("geoc_raster_crop_with_layer_command", [bufferLayer, raster, pointsLayer])
    }

    @Test
    void contour() {
        String command = "geoc raster contour -i src/test/resources/pc.tif -b 0 -v 300 -s -m -o target/contours.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_contour_command", command)
        writeTextFile("geoc_raster_contour_command_output", result)

        Raster raster = new GeoTIFF(new File("src/test/resources/pc.tif")).read()
        raster.style = new ColorMap([
                [color: "#9fd182", quantity:25],
                [color: "#3e7f3c", quantity:470],
                [color: "#133912", quantity:920],
                [color: "#08306b", quantity:1370],
                [color: "#fffff5", quantity:1820],
        ])
        Layer contours = new Shapefile("target/contours.shp")
        contours.style = new Stroke("black", 0.5)
        draw("geoc_raster_contour_command", [raster, contours], raster.bounds)
    }

    @Test
    void convolve() {
        String command = "geoc raster convolve -i src/test/resources/pc.tif -o target/pc_convolve.tif -w 2 -h 2"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_convolve_command", command)
        writeTextFile("geoc_raster_convolve_command_output", result)

        [
                [name: "orginal", file: "src/test/resources/pc.tif"],
                [name: "convolved", file: "target/pc_convolve.tif"]
        ].each { Map values ->
            String cmd = "geoc raster info -i ${values.file}"
            writeTextFile("geoc_raster_convolve_command_${values.name}_cmd", cmd)
            writeTextFile("geoc_raster_convolve_command_${values.name}_result", runApp(cmd))

        }

        Raster convolvedRaster = Format.getFormat(new File("target/pc_convolve.tif")).read()
        convolvedRaster.style = new ColorMap([
                [color: "#9fd182", quantity: -32767.0],
                [color: "#3e7f3c", quantity: -15000.0],
                [color: "#133912", quantity: 0.3],
                [color: "#08306b", quantity: 15000.0],
                [color: "#fffff5", quantity: 32767.0],
        ])
        draw("geoc_raster_convolve_command", [convolvedRaster])
    }


    @Test
    void display() {
        String command = "geoc raster display -i src/test/resources/pc.tif"
        writeTextFile("geoc_raster_display_command", command)
    }

    @Test
    void envelope() {
        String command = "geoc raster envelope -i src/test/resources/earth.tif -o target/earth_envelope.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_envelope_command", command)
        writeTextFile("geoc_raster_envelope_command_output", result)

        Raster raster = new GeoTIFF(new File("src/test/resources/earth.tif")).read()
        Layer layer = new Shapefile("target/earth_envelope.shp")
        layer.style = new Stroke("red", 2)
        drawOnBasemap("geoc_raster_envelope_command", [raster, layer])
    }

    @Test
    void info() {
        String command = "geoc raster info -i src/test/resources/earth.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_info_command", command)
        writeTextFile("geoc_raster_info_command_output", result)
    }

    @Test
    void getSize() {
        String command = "geoc raster size -i src/test/resources/earth.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_getsize_command", command)
        writeTextFile("geoc_raster_getsize_command_output", result)
    }

    @Test
    void getProjection() {
        String command = "geoc raster projection -i src/test/resources/earth.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_getprojection_command", command)
        writeTextFile("geoc_raster_getprojection_command_output", result)
    }

    @Test
    void reclassify() {
        String command = "geoc raster reclassify -i src/test/resources/pc.tif -o target/pc_reclass.tif -r 0-0=1 -r 0-50=2 -r 50-200=3 -r 200-1000=5 -r 1000-1500=4 -r 1500-4000=6"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_reclassify_command", command)
        writeTextFile("geoc_raster_reclassify_command_output", result)

        Raster raster = new GeoTIFF(new File("target/pc_reclass.tif")).read()
        raster.style = new ColorMap([
                [quantity: 1, color: "#FFFACD"],
                [quantity: 2, color: "#F0E68C"],
                [quantity: 3, color: "#DAA520"],
                [quantity: 4, color: "#FF4500"],
                [quantity: 5, color: "#800000"],
                [quantity: 6, color: "#F5FFFA"]
        ])
        draw("geoc_raster_reclassify_command", [raster])
    }

    @Test
    void wordFile() {
        String command = "geoc raster worldfile -b 10,11,20,21 -s 800,751"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_worldfile_command", command)
        writeTextFile("geoc_raster_worldfile_command_output", result)
    }

}
