package org.geocommands.doc

import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.style.io.SimpleStyleReader
import org.junit.jupiter.api.Test

class GeometryTest extends DocTest {

    @Test
    void convert() {
        List commands = ["geometry", "convert", "-i",  "POINT(-122.386371 47.581154)", "-f", "geojson", "-t", "feature"]
        String command = "geoc " + commands.collect{
            it.startsWith("POINT(") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_convert_wkt_geojson_feature_command", command)
        writeTextFile("geoc_geometry_convert_wkt_geojson_feature_command_output", result)
    }

    @Test
    void decimalDegrees2Point() {
        List commands = ["geometry dd2pt", "-d", "122d 31m 32.23s W, 47d 12m 43.28s N", "-t", "wkt"]
        String command = "geoc " + commands.collect{
            it.startsWith("122d") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_dd2pt_command", command)
        writeTextFile("geoc_geometry_dd2pt_command_output", result)
    }

    @Test
    void geoHashBounds() {
        List commands = ["geometry geohash bounds", "-b", "120, 30, 120.0001, 30.0001", "-t", "long", "-d", "45"]
        String command = "geoc " + commands.collect{
            it.startsWith("120") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_geohash_bounds_command", command)
        writeTextFile("geoc_geometry_geohash_bounds_command_output", result)
    }

    @Test
    void geoHashDecode() {
        String command = "geoc geometry geohash decode -i uf8vk6wjr -t point"
        String result = runApp(command, "")
        writeTextFile("geoc_geometry_geohash_decode_command", command)
        writeTextFile("geoc_geometry_geohash_decode_command_output", result)
    }

    @Test
    void geoHashEncode() {
        List commands = ["geometry", "geohash", "encode", "-i",  "POINT(-122.386371 47.581154)"]
        String command = "geoc " + commands.collect{
            it.startsWith("POINT(") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_geohash_encode_command", command)
        writeTextFile("geoc_geometry_geohash_encode_command_output", result)
    }

    @Test
    void geoHashNeighbors() {
        String command = "geoc geometry geohash neighbors -i uf8vk6wjr"
        String result = runApp(command, "")
        writeTextFile("geoc_geometry_geohash_neighbors_command", command)
        writeTextFile("geoc_geometry_geohash_neighbors_command_output", result)
    }

    @Test
    void greatCircleArc() {
        List commands = ["geometry", "greatcirclearc", "-p",  "POINT (-122 48)", "-t", "POINT (-0.102938 51.498749)", "-e", "wgs84", "-n", "20"]
        String command = "geoc " + commands.collect{
            it.startsWith("POINT(") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_greatcirclearc_command", command)
        writeTextFile("geoc_geometry_greatcirclearc_command_output", result)

        Layer layer = Layer.fromGeometry("greatciclearc", Geometry.fromWKT(result))
        layer.style = new SimpleStyleReader().read("stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_geometry_greatcirclearc_command", [layer])
    }

    @Test
    void offset() {
        String geometry = "LINESTRING (-120.41362631285119 47.87883318858252, -3.9909723099333974 39.24424611524387)"
        List commands = ["geometry", "offset", "-i", geometry, "-d", "5", "-s", "8"]
        String command = "geoc " + commands.collect{
            it.startsWith("LINESTRING(") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_offset_command", command)
        writeTextFile("geoc_geometry_offset_command_output", result)

        Layer inLayer = Layer.fromGeometry("geometry", Geometry.fromWKT(geometry))
        Layer outLayer = Layer.fromGeometry("offset", Geometry.fromWKT(result))
        inLayer.style = new SimpleStyleReader().read("stroke=#555555 stroke-width=0.5")
        outLayer.style = new SimpleStyleReader().read("stroke=red stroke-width=0.5")
        drawOnBasemap("gppteoc_geometry_offset_command", [inLayer, outLayer])
    }

    @Test
    void orthodromicDistance() {
        List commands = ["geometry", "orthodromicdistance", "-p",  "POINT (-122 48)", "-t", "POINT (-0.102938 51.498749)", "-e", "wgs84"]
        String command = "geoc " + commands.collect{
            it.startsWith("POINT(") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_orthodromicdistance_command", command)
        writeTextFile("geoc_geometry_orthodromicdistance_command_output", result)
    }

    @Test
    void plot() {
        String geometry = "POLYGON ((-113.98365269610397 52.04260423559353, -117.55190821991903 41.99216856357597, -102.82940482544078 37.1267755781612, -82.26457660787091 47.05513909003821, -102.75935045963138 44.33220905070587, -101.89775634863287 52.5472919646931, -113.98365269610397 52.04260423559353))"
        List commands = ["geometry", "plot", "-i", geometry, "-f", "target/geometry_plot.png"]
        String command = "geoc " + commands.collect{
            it.startsWith("POLYGON") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_plot_command", command)
        copyFile(new File("target/geometry_plot.png"), new File("src/main/docs/images/geoc_geometry_plot_command.png"))
    }

    @Test
    void point2DecimalDegrees() {
        List commands = ["geometry", "pt2dd", "-p", "POINT (-122 48)", "-t", "dms"]
        String command = "geoc " + commands.collect{
            it.startsWith("POINT") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_geometry_pt2dd_command", command)
        writeTextFile("geoc_geometry_pt2dd_command_output", result)
    }

}
