package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.ProjectCommand.ProjectOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The ProjectCommand Unit Test
 * @author Jared Erickson
 */
class ProjectCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points2927.properties")
        File shpFile = createTemporaryShapefile("points")
        ProjectCommand cmd = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile.absolutePath,
                sourceProjection: "EPSG:2927",
                targetProjection: "EPSG:4326"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ
        assertEquals "EPSG:4326", shp.proj.id
        double delta = 0.0000001
        assertPointsAreEqual(Point.fromWKT("POINT (-122.36984341006178 47.124966381776666)"),shp.features[0].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.36984341006178 47.124966381776666)"), shp.features[0].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.21239542729938 47.019696836485686)"), shp.features[1].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.29600512408041 47.24905202092161)"),  shp.features[2].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.77250760440084 47.250820176480524)"), shp.features[3].geom, delta)
    }

    @Test
    void executeWithCsv() {
        ProjectCommand cmd = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                sourceProjection: "EPSG:2927",
                targetProjection: "EPSG:4326"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points2927.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        double delta = 0.0000001
        assertPointsAreEqual(Point.fromWKT("POINT (-122.36984341006178 47.124966381776666)"), layer.features[0].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.21239542729938 47.019696836485686)"), layer.features[1].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.29600512408041 47.24905202092161)"), layer.features[2].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.77250760440084 47.250820176480524)"), layer.features[3].geom, delta)
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points2927.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector project",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-s", "EPSG:2927",
                "-t", "EPSG:4326"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ
        assertEquals "EPSG:4326", shp.proj.id
        double delta = 0.0000001
        assertPointsAreEqual(Point.fromWKT("POINT (-122.36984341006178 47.124966381776666)"), shp.features[0].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.21239542729938 47.019696836485686)"), shp.features[1].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.29600512408041 47.24905202092161)"),  shp.features[2].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.77250760440084 47.250820176480524)"), shp.features[3].geom, delta)

        String output = runApp(["vector project", "-s", "EPSG:2927", "-t", "EPSG:4326"], readCsv("points2927.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertPointsAreEqual(Point.fromWKT("POINT (-122.36984341006178 47.124966381776666)"), layer.features[0].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.21239542729938 47.019696836485686)"), layer.features[1].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.29600512408041 47.24905202092161)"), layer.features[2].geom, delta)
        assertPointsAreEqual(Point.fromWKT("POINT (-122.77250760440084 47.250820176480524)"), layer.features[3].geom, delta)
    }

}
