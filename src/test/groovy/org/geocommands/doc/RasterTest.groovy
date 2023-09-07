package org.geocommands.doc

import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.style.ColorMap
import geoscript.style.Label
import geoscript.style.Stroke
import geoscript.style.Style
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
    void crop() {
        String command = "geoc raster crop -i src/test/resources/earth.tif -b -160.927734,6.751896,-34.716797,57.279043 -o target/earth_cropped.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_crop_command", command)
        writeTextFile("geoc_raster_crop_command_output", result)

        Raster raster = new GeoTIFF(new File("target/earth_cropped.tif")).read()
        drawOnBasemap("geoc_raster_crop_command", [raster])
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
