package org.geocommands.geometry

import groovy.json.JsonSlurper
import org.geocommands.BaseTest
import org.geocommands.geometry.ConvertCommand.ConvertOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The ConvertCommand Unit Test
 * @author Jared Erickson
 */
class ConvertCommandTest extends BaseTest {

    @Test
    void executeWktToGeoJSONGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geojson"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("{\"type\":\"Point\",\"coordinates\":[1,2]}", writer.toString())
    }

    @Test
    void executeWktToGeoJSONGeometryFromInput() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                input: "POINT (1 2)",
                format: "geojson"
        )
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("{\"type\":\"Point\",\"coordinates\":[1,2]}", writer.toString())
    }

    @Test
    void executeWktToGeoJSONFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geojson",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1,2]},\"properties\":{},\"id\":\"1\"}", writer.toString())
    }

    @Test
    void executeWktToGeoJSONLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geojson",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        def json = new JsonSlurper().parseText(writer.toString())
        assertEquals "FeatureCollection", json.type
        assertEquals 1, json.features.size()
        assertEquals "Feature", json.features[0].type
        assertEquals "Point", json.features[0].geometry.type
        assertEquals 1, json.features[0].geometry.coordinates[0]
        assertEquals 2, json.features[0].geometry.coordinates[1]
        assertNotNull json.features[0].properties
        assertNotNull json.features[0].id
    }

    @Test
    void runWktToKmlGeometry() {
        String actual = runApp([
                "geometry convert",
                "-f", "kml"
        ], "POINT (1 2)").trim()
        String expected = "<Point><coordinates>1.0,2.0</coordinates></Point>"
        assertEquals(expected, actual)
    }

    @Test
    void runWktToKmlFeature() {
        String actual = runApp([
                "geometry convert",
                "-f", "kml",
                "-t", "feature"
        ], "POINT (1 2)").trim()
        String expected = "<kml:Placemark xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:kml=\"http://earth.google.com/kml/2.1\" id=\"1\">" + NEW_LINE +
                "<kml:Point>" + NEW_LINE +
                "<kml:coordinates>1.0,2.0</kml:coordinates>" + NEW_LINE +
                "</kml:Point>" + NEW_LINE +
                "</kml:Placemark>"
        assertEquals(expected, actual)
    }

    @Test
    void runWktToGmlFeature() {
        String actual = runApp([
                "geometry convert",
                "-i", "<Point><coordinates>1.0,2.0</coordinates></Point>",
                "-f", "gml",
                "-t", "feature"
        ], "").trim()
        String expected = "<gsf:feature xmlns:gsf=\"http://geoscript.org/feature\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\" fid=\"1\">" + NEW_LINE +
                "<gsf:geom>" + NEW_LINE +
                "<gml:Point>" + NEW_LINE +
                "<gml:coord>" + NEW_LINE +
                "<gml:X>1.0</gml:X>" + NEW_LINE +
                "<gml:Y>2.0</gml:Y>" + NEW_LINE +
                "</gml:coord>" + NEW_LINE +
                "</gml:Point>" + NEW_LINE +
                "</gsf:geom>" + NEW_LINE +
                "</gsf:feature>"
        assertStringsEqual(expected, actual, true)
    }
}
