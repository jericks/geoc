package org.geocommands.doc

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import geoscript.layer.io.GeoJSONReader
import geoscript.proj.Projection
import geoscript.style.Fill
import geoscript.style.Shape
import geoscript.style.io.SimpleStyleReader
import geoscript.workspace.GeoPackage
import geoscript.workspace.Workspace
import org.junit.jupiter.api.Test

class VectorTest extends DocTest {

    @Test
    void add() {
        runApp(["vector", "create", "-o", "target/locations.shp", "-f", "the_geom=POINT EPSG:4326", "-f", "id=integer", "-f", "name=string"], "")
        List commands = ["vector", "add", "-i", "target/locations.shp", "-v", "id=1", "-v", "name=Seattle", "-v", "the_geom=POINT (-122.334758 47.578364)"]
        String command = "geoc " + commands.collect{
            it.startsWith("the_geom") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_vector_add_command", command)
        writeTextFile("geoc_vector_add_command_values", createFeatureTable(new Shapefile("target/locations.shp")))

        Layer layer = new Shapefile("target/locations.shp")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555 label=name label-size=14")
        drawOnBasemap("geoc_vector_add_command", [layer])
    }

    @Test
    void addfields() {
        runApp(["vector", "create", "-o", "target/locations.shp", "-f", "the_geom=POINT EPSG:4326"], "")
        List commands = ["vector", "addfields", "-i", "target/locations.shp", "-o", "target/locations_idname.shp", "-f", "id=int", "-f", "name=string"]
        String command = "geoc " + commands.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_vector_addfields_command", command)
        writeTextFile("geoc_vector_addfields_command_schema", createSchemaTable(new Shapefile("target/locations_idname.shp").schema))
    }

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
        writeTextFile("geoc_vector_addxyfields_command", command)

        Layer layer = new Shapefile("target/places_xy.shp")
        writeTextFile("geoc_vector_addxyfields_command_schema", createSchemaTable(layer.schema, ["the_geom", "NAME", "x_coord", "y_coord"]))
        writeTextFile("geoc_vector_addxyfields_command_values", createFeatureTable(layer, ["NAME", "x_coord", "y_coord"], 5))
    }

    @Test
    void append() {
        runApp(["vector", "randompoints", "-o", "target/points1.shp", "-g", "-180,-90,180,90", "-n", "5"], "")
        runApp(["vector", "randompoints", "-o", "target/points2.shp", "-g", "-180,-90,180,90", "-n", "5"], "")
        runApp(["vector", "copy", "-i", "target/points1.shp", "-o", "target/points1_original.shp"], "")
        List commands = ["vector", "append", "-i", "target/points1.shp", "-k", "target/points2.shp"]
        String command = "geoc " + commands.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_vector_append_command", command)

        Layer layer1 = new Shapefile("target/points1_original.shp").buffer(4)
        Layer layer2 = new Shapefile("target/points2.shp").buffer(4)
        Layer layerAll = new Shapefile("target/points1.shp")
        layer1.style = new Fill("green")
        layer2.style = new Fill("blue")
        layerAll.style = new Shape("yellow")
        drawOnBasemap("geoc_vector_append_command", [layer1, layer2, layerAll])
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
    void create() {
        List commands = ["vector", "create", "-o", "target/locations.shp", "-f", "the_geom=POINT EPSG:4326", "-f", "id=integer", "-f", "name=string"]
        String command = "geoc " + commands.collect{
            it.startsWith("the_geom") ? '"' + it + '"' : it
        }.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_vector_create_command", command)
        writeTextFile("geoc_vector_create_command_schema", createSchemaTable(new Shapefile("target/locations.shp").schema))
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
    void fromGeoJson() {
        runApp("geoc vector randompoints -n 5 -g -180,-90,180,90 -o target/randompoints.shp","")
        String str = runApp("geoc vector to -i target/randompoints.shp -f geojson","")
        File file = new File("target/randompoints.json")
        file.text = str
        String command = "geoc vector from -f csv"
        String result = runApp(command, str)
        writeTextFile("geoc_vector_from_geojson_command", "cat points.json | ${command}")
        writeTextFile("geoc_vector_from_geojson_command_input", str)

        Layer layer = new GeoJSONReader().read(file)
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_from_geojson_command", [layer])
    }

    @Test
    void fromCsv() {
        runApp("geoc vector randompoints -n 5 -g -180,-90,180,90 -o target/randompoints.shp","")
        String str = runApp("geoc vector to -i target/randompoints.shp -f csv","")
        File file = new File("target/randompoints.csv")
        file.text = str
        String command = "geoc vector from -f csv"
        String result = runApp(command, str)
        writeTextFile("geoc_vector_from_csv_command", "cat points.csv | ${command}")
        writeTextFile("geoc_vector_from_csv_command_input", str)

        Layer layer = new CsvReader().read(result)
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_from_csv_command", [layer])
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
    void layerList() {
        String command = "geoc vector list layers -i src/test/resources/data.gpkg"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_list_layers_command", command)
        writeTextFile("geoc_vector_list_layers_command_output", result)
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
    void project() {
        String command = "geoc vector project -i src/test/resources/data.gpkg -l places -o target/mercator.gpkg -r places -s EPSG:4326 -t EPSG:3857"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_project_command", command)

        Layer layer = new GeoPackage("target/mercator.gpkg").get("places")
        layer.style = new SimpleStyleReader().read("shape-type=circle shape-size=8 shape=#555555")
        drawOnBasemap("geoc_vector_project_command", [layer], proj: "EPSG:3857")
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
    void schema() {
        String command = "geoc vector schema -i src/test/resources/data.gpkg -l countries -p"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_schema_command", command)
        writeTextFile("geoc_vector_schema_command_output", result)
    }

    @Test
    void toGeoJson() {
        runApp("geoc vector randompoints -n 5 -g -180,-90,180,90 -o target/randompoints.shp","")
        println new Shapefile("target/randompoints.shp").features
        String command = "geoc vector to -i target/randompoints.shp -f geojson"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_to_command", command)
        writeTextFile("geoc_vector_to_command_output", result)
    }

    @Test
    void toCsv() {
        runApp("geoc vector randompoints -n 5 -g -180,-90,180,90 -o target/randompoints.shp","")
        println new Shapefile("target/randompoints.shp").features
        String command = "geoc vector to -i target/randompoints.shp -f csv"
        String result = runApp(command, "")
        writeTextFile("geoc_vector_to_csv_command", command)
        writeTextFile("geoc_vector_to_csv_command_output", result)
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
