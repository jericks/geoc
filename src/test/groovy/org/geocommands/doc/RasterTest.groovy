package org.geocommands.doc

import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.style.ColorMap
import geoscript.style.Stroke
import org.junit.jupiter.api.Test

class RasterTest extends DocTest {

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
    void wordFile() {
        String command = "geoc raster worldfile -b 10,11,20,21 -s 800,751"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_worldfile_command", command)
        writeTextFile("geoc_raster_worldfile_command_output", result)
    }

}
