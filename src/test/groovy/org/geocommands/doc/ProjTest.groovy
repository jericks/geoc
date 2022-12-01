package org.geocommands.doc

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.style.io.SimpleStyleReader
import org.junit.jupiter.api.Test

class ProjTest extends DocTest {

    @Test
    void envelope() {
        String command = "geoc proj envelope -e EPSG:2927 -g -o target/envelope.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_proj_envelope_command", command)

        Layer layer = new Shapefile("target/envelope.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_proj_envelope_command", [layer], bounds: layer.bounds.expandBy(10))
    }

    @Test
    void wkt() {
        String command = "geoc proj wkt -e EPSG:2927"
        String result = runApp(command, "")
        writeTextFile("geoc_proj_wkt_command", command)
        writeTextFile("geoc_proj_wkt_command_output", result)
    }

}
