package org.geocommands.doc

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.Pbf
import geoscript.style.io.SimpleStyleReader
import geoscript.workspace.GeoPackage
import geoscript.workspace.Workspace
import org.junit.jupiter.api.Test

class VectorTest extends DocTest {

    @Test
    void addareafield() {
        String command = "geoc vector addareafield -i src/test/resources/states.shp -o target/states_area.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_addareafield_command", command)

        Layer layer = new Shapefile("target/states_area.shp")
        writeTextFile("geoc_vector_addareafield_command_schema", createSchemaTable(layer.schema, ["the_geom", "STATE_NAME", "SUB_REGION", "STATE_ABBR", "AREA"]))
        writeTextFile("geoc_vector_addareafield_command_values", createFeatureTable(layer, ["STATE_NAME", "SUB_REGION", "STATE_ABBR", "AREA"], 5))
    }

    @Test
    void addlengthfield() {
        String command = "geoc vector addlengthfield -i src/test/resources/data.gpkg -l rivers -o target/rivers_length.shp -f length"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_addlengthfield_command", command)

        Layer layer = new Shapefile("target/rivers_length.shp")
        writeTextFile("geoc_vector_addlengthfield_command_schema", createSchemaTable(layer.schema, ["the_geom", "name", "label", "length"]))
        writeTextFile("geoc_vector_addlengthfield_command_values", createFeatureTable(layer, ["name", "label", "length"], 5))
    }

    @Test
    void addxyfields() {
        String command = "geoc vector addxyfields -i src/test/resources/data.gpkg -l places -o target/places_xy.shp -x x_coord -y y_coord -a centroid"
        String result = runApp(command, "")
        println result
        writeTextFile("geoc_vector_addxyfields_command", command)

        Layer layer = new Shapefile("target/places_xy.shp")
        writeTextFile("geoc_vector_addxyfields_command_schema", createSchemaTable(layer.schema, ["the_geom", "NAME", "x_coord", "y_coord"]))
        writeTextFile("geoc_vector_addxyfields_command_values", createFeatureTable(layer, ["NAME", "x_coord", "y_coord"], 5))
    }

    @Test
    void buffer() {
        String command = "geoc vector buffer -i src/test/resources/data.gpkg -l places -o target/places_buffer.shp -d 10"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_buffer_command", command)
        writeTextFile("geoc_vector_buffer_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/places_buffer.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_buffer_command", [layer, placesLayer])
    }

    @Test
    void centroid() {
        String command = "geoc vector centroid -i src/test/resources/data.gpkg -l countries -o target/countries_centroids.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_centroid_command", command)
        writeTextFile("geoc_vector_centroid_command_output", result)

        Layer layer = new Shapefile("target/countries_centroids.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_centroid_command", [layer])
    }

    @Test
    void convexHull() {
        String command = "geoc vector convexhull -i src/test/resources/data.gpkg -l places -o target/convexhull.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_convexhull_command", command)
        writeTextFile("geoc_vector_convexhull_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/convexhull.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_convexhull_command", [layer, placesLayer])
    }

    @Test
    void convexHulls() {
        String command = "geoc vector convexhulls -i src/test/resources/data.gpkg -l countries -o target/convexhulls.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_convexhulls_command", command)
        writeTextFile("geoc_vector_convexhulls_command_output", result)

        Layer layer = new Shapefile("target/convexhulls.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_convexhulls_command", [layer])
    }

    @Test
    void count() {
        String command = "geoc vector count -i src/test/resources/data.gpkg -l places"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_count_command", command)
        writeTextFile("geoc_vector_count_command_output", result)
    }

    @Test
    void delaunay() {
        String command = "geoc vector delaunay -i src/test/resources/data.gpkg -l places -o target/delaunay.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_delaunay_command", command)
        writeTextFile("geoc_vector_delaunay_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/delaunay.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_delaunay_command", [layer, placesLayer], bounds: placesLayer.bounds)
    }

    @Test
    void ellipse() {
        String command = "geoc vector ellipse -i src/test/resources/data.gpkg -l countries -o target/ellipse.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_ellipse_command", command)
        writeTextFile("geoc_vector_ellipse_command_output", result)

        Layer layer = new Shapefile("target/ellipse.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_ellipse_command", [layer])
    }

    @Test
    void envelope() {
        String command = "geoc vector envelope -i src/test/resources/data.gpkg -l places -o target/envelope.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_envelope_command", command)
        writeTextFile("geoc_vector_envelope_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/envelope.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_envelope_command", [layer, placesLayer])
    }

    @Test
    void envelopes() {
        String command = "geoc vector envelopes -i src/test/resources/data.gpkg -l countries -o target/envelopes.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_envelopes_command", command)
        writeTextFile("geoc_vector_envelopes_command_output", result)

        Layer layer = new Shapefile("target/envelopes.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_envelopes_command", [layer])
    }

    @Test
    void graticuleSquare() {
        String command = "geoc vector graticule square -g -180,-90,180,90 -l 20 -o target/squares.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_graticule_square_command", command)
        writeTextFile("geoc_vector_graticule_square_command_output", result)

        Layer layer = new Shapefile("target/squares.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_graticule_square_command", [layer])
    }

    @Test
    void graticuleRectangle() {
        String command = "geoc vector graticule rectangle -g -180,-90,180,90 -w 10 -h 20 -o target/rectangles.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_graticule_rectangle_command", command)
        writeTextFile("geoc_vector_graticule_rectangle_command_output", result)

        Layer layer = new Shapefile("target/rectangles.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_graticule_rectangle_command", [layer])
    }

    @Test
    void graticuleHexagon() {
        String command = "geoc vector graticule hexagon -g -180,-90,180,90 -l 10 -o target/hexagons.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_graticule_hexagon_command", command)
        writeTextFile("geoc_vector_graticule_hexagon_command_output", result)

        Layer layer = new Shapefile("target/hexagons.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_graticule_hexagon_command", [layer])
    }

    @Test
    void graticuleOval() {
        String command = "geoc vector graticule oval -g -180,-90,180,90 -l 20 -o target/ovals.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_graticule_oval_command", command)
        writeTextFile("geoc_vector_graticule_oval_command_output", result)

        Layer layer = new Shapefile("target/ovals.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_graticule_oval_command", [layer])
    }

    @Test
    void graticuleLine() {
        String command = "geoc vector graticule line -g -180,-90,180,90 -l vertical,2,10 -l horizontal,1,2 -o target/lines.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_graticule_line_command", command)
        writeTextFile("geoc_vector_graticule_line_command_output", result)

        Layer layer = new Shapefile("target/lines.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_graticule_line_command", [layer])
    }

    @Test
    void info() {
        String command = "geoc vector info -i src/test/resources/data.gpkg -l countries"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_info_command", command)
        writeTextFile("geoc_vector_info_command_output", result.split("\n").take(22).join("\n"))
    }

    @Test
    void interiorPoint() {
        String command = "geoc vector interiorPoint -i src/test/resources/data.gpkg -l countries -o target/countries_interiorpoints.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_interiorpoint_command", command)
        writeTextFile("geoc_vector_interiorpoint_command_output", result)

        Layer layer = new Shapefile("target/countries_interiorpoints.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_interiorpoint_command", [layer])
    }

    @Test
    void mincircle() {
        String command = "geoc vector mincircle -i src/test/resources/data.gpkg -l places -o target/mincircle.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_mincircle_command", command)
        writeTextFile("geoc_vector_mincircle_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/mincircle.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_mincircle_command", [layer, placesLayer])
    }

    @Test
    void mincircles() {
        String command = "geoc vector mincircles -i src/test/resources/data.gpkg -l countries -o target/mincircles.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_mincircles_command", command)
        writeTextFile("geoc_vector_mincircles_command_output", result)

        Layer layer = new Shapefile("target/mincircles.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_mincircles_command", [layer], bounds: new Bounds(-180,-90,180,90, "EPSG:4326"))
    }

    @Test
    void minrect() {
        String command = "geoc vector minrect -i src/test/resources/data.gpkg -l places -o target/minrect.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_minrect_command", command)
        writeTextFile("geoc_vector_minrect_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/minrect.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_minrect_command", [layer, placesLayer])
    }

    @Test
    void minrects() {
        String command = "geoc vector minrects -i src/test/resources/data.gpkg -l countries -o target/minrects.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_minrects_command", command)
        writeTextFile("geoc_vector_minrects_command_output", result)

        Layer layer = new Shapefile("target/minrects.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_minrects_command", [layer], bounds: new Bounds(-180,-90,180,90, "EPSG:4326"))
    }

    @Test
    void randompoints() {
        String command = "geoc vector randompoints -n 100 -g -180,-90,180,90 -o target/randompoints.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_randompoints_command", command)
        writeTextFile("geoc_vector_randompoints_command_output", result)

        Layer layer = new Shapefile("target/randompoints.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_randompoints_command", [layer])
    }

    @Test
    void voronoi() {
        String command = "geoc vector voronoi -i src/test/resources/data.gpkg -l places -o target/voronoi.shp"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_voronoi_command", command)
        writeTextFile("geoc_vector_voronoi_command_output", result)

        Workspace workspace = new GeoPackage("src/test/resources/data.gpkg")
        Layer placesLayer = workspace.get("places")
        placesLayer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")

        Layer layer = new Shapefile("target/voronoi.shp")
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_vector_voronoi_command", [layer, placesLayer], bounds: placesLayer.bounds)
    }

}
