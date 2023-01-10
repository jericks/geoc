package org.geocommands.doc

import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Shapefile
import geoscript.style.Stroke
import geoscript.style.io.SimpleStyleReader
import org.junit.jupiter.api.Test

class RasterTest extends DocTest {

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
