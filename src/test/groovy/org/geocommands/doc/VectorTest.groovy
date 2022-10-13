package org.geocommands.doc

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.style.io.SimpleStyleReader
import geoscript.workspace.GeoPackage
import geoscript.workspace.Workspace
import org.junit.jupiter.api.Test

class VectorTest extends DocTest {

    @Test
    void buffer() {
        String command = "geoc vector buffer -i src/test/resources/data.gpkg -l places -o target/places_buffer.shp -d 10"
        String result = runApp(command, "")
        writeTextFile("geoc_buffer_command", command)
        writeTextFile("geoc_buffer_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/places_buffer.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_buffer_command", [layer, placesLayer])
    }

    @Test
    void centroid() {
        String command = "geoc vector centroid -i src/test/resources/data.gpkg -l countries -o target/countries_centroids.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_centroid_command", command)
        writeTextFile("geoc_centroid_command_output", result)

        Layer layer = new Shapefile("target/countries_centroids.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_centroid_command", [layer])
    }

    @Test
    void convexHull() {
        String command = "geoc vector convexhull -i src/test/resources/data.gpkg -l places -o target/convexhull.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_convexhull_command", command)
        writeTextFile("geoc_convexhull_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/convexhull.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_convexhull_command", [layer, placesLayer])
    }

    @Test
    void convexHulls() {
        String command = "geoc vector convexhulls -i src/test/resources/data.gpkg -l countries -o target/convexhulls.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_convexhulls_command", command)
        writeTextFile("geoc_convexhulls_command_output", result)

        Layer layer = new Shapefile("target/convexhulls.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_convexhulls_command", [layer])
    }

    @Test
    void envelope() {
        String command = "geoc vector envelope -i src/test/resources/data.gpkg -l places -o target/envelope.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_envelope_command", command)
        writeTextFile("geoc_envelope_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/envelope.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_envelope_command", [layer, placesLayer])
    }

    @Test
    void envelopes() {
        String command = "geoc vector envelopes -i src/test/resources/data.gpkg -l countries -o target/envelopes.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_envelopes_command", command)
        writeTextFile("geoc_envelopes_command_output", result)

        Layer layer = new Shapefile("target/envelopes.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_envelopes_command", [layer])
    }

    @Test
    void interiorPoint() {
        String command = "geoc vector interiorPoint -i src/test/resources/data.gpkg -l countries -o target/countries_interiorpoints.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_interiorpoint_command", command)
        writeTextFile("geoc_interiorpoint_command_output", result)

        Layer layer = new Shapefile("target/countries_interiorpoints.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_interiorpoint_command", [layer])
    }

    @Test
    void mincircle() {
        String command = "geoc vector mincircle -i src/test/resources/data.gpkg -l places -o target/mincircle.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_mincircle_command", command)
        writeTextFile("geoc_mincircle_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/mincircle.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_mincircle_command", [layer, placesLayer])
    }

    @Test
    void mincircles() {
        String command = "geoc vector mincircles -i src/test/resources/data.gpkg -l countries -o target/mincircles.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_mincircles_command", command)
        writeTextFile("geoc_mincircles_command_output", result)

        Layer layer = new Shapefile("target/mincircles.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_mincircles_command", [layer], bounds: new Bounds(-180,-90,180,90, "EPSG:4326"))
    }

    @Test
    void minrect() {
        String command = "geoc vector minrect -i src/test/resources/data.gpkg -l places -o target/minrect.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_minrect_command", command)
        writeTextFile("geoc_minrect_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/minrect.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_minrect_command", [layer, placesLayer])
    }

    @Test
    void minrects() {
        String command = "geoc vector minrects -i src/test/resources/data.gpkg -l countries -o target/minrects.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_minrects_command", command)
        writeTextFile("geoc_minrects_command_output", result)

        Layer layer = new Shapefile("target/minrects.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_minrects_command", [layer], bounds: new Bounds(-180,-90,180,90, "EPSG:4326"))
    }

    @Test
    void voronoi() {
        String command = "geoc vector voronoi -i src/test/resources/data.gpkg -l places -o target/voronoi.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_voronoi_command", command)
        writeTextFile("geoc_voronoi_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/voronoi.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_voronoi_command", [layer, placesLayer], bounds: placesLayer.bounds)
    }

}
