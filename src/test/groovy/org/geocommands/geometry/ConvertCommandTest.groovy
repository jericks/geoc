package org.geocommands.geometry

import groovy.json.JsonSlurper
import org.geocommands.BaseTest
import org.geocommands.geometry.ConvertCommand.ConvertOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The ConvertCommand Unit Test
 * @author Jared Erickson
 */
class ConvertCommandTest extends BaseTest {

    @Test
    void executeGeoJSONToWKTGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "wkt"
        )
        StringReader reader = new StringReader("{\"type\":\"Point\",\"coordinates\":[1,2]}")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("POINT (1 2)", writer.toString())
    }

    @Test
    void executeWKTToWKBGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "wkb"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("00000000013FF00000000000004000000000000000", writer.toString())
    }

    @Test
    void executeWKTToGeobufGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geobuf"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("10021806320b08001a0780897a8092f401", writer.toString())
    }

    @Test
    void executeWKTToKMLGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "kml"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<Point><coordinates>1.0,2.0</coordinates></Point>", writer.toString())
    }

    @Test
    void executeWKTToGMLGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gml"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<gml:Point><gml:coordinates>1.0,2.0</gml:coordinates></gml:Point>", writer.toString())
    }

    @Test
    void executeWKTToGML3GeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gml3"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<gml:Point><gml:pos>1.0 2.0</gml:pos></gml:Point>", writer.toString())
    }

    @Test
    void executeWKTToGeoRSSGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "georss"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<georss:point>2.0 1.0</georss:point>", writer.toString())
    }

    @Test
    void executeWKTToGPXGeometryFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gpx"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<wpt lat='2.0' lon='1.0'/>", writer.toString())
    }

    @Test
    void executeWktToGmlFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gml",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals(stripXmlNS("<gsf:feature xmlns:gsf=\"http://geoscript.org/feature\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\" fid=\"1\">" + NEW_LINE +
                "<gsf:geom>" + NEW_LINE +
                "<gml:Point>" + NEW_LINE +
                "<gml:coord>" + NEW_LINE +
                "<gml:X>1.0</gml:X>" + NEW_LINE +
                "<gml:Y>2.0</gml:Y>" + NEW_LINE +
                "</gml:coord>" + NEW_LINE +
                "</gml:Point>" + NEW_LINE +
                "</gsf:geom>" + NEW_LINE +
                "</gsf:feature>" + NEW_LINE), stripXmlNS(writer.toString()))
    }

    @Test
    void executeWktToGeobufFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geobuf",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("100218062a0d0a0b08001a0780897a8092f401", writer.toString())
    }

    @Test
    void executeWktToKmlFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "kml",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<kml:Placemark xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:kml=\"http://earth.google.com/kml/2.1\" id=\"1\">" +
                "<kml:Point>" +
                "<kml:coordinates>1.0,2.0</kml:coordinates>" +
                "</kml:Point>" +
                "</kml:Placemark>", writer.toString())
    }

    @Test
    void executeWktToGeoRssFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "georss",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)

        String actual = writer.toString()
        assertTrue(actual.startsWith("<entry"))
        assertTrue(actual.endsWith("</entry>"))
    }

    @Test
    void executeWktToGpxFeatureFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gpx",
                type: "feature"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("<wpt lat='2.0' lon='1.0' xmlns='http://www.topografix.com/GPX/1/1'><name>1</name></wpt>", writer.toString())
    }

    // Layer

    @Test
    void executeWktToCsvLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "csv",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("""\"geom:Point\"
\"POINT (1 2)\"
""", writer.toString())
    }

    @Test
    void executeWktToGeobufLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geobuf",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertEquals("10021806220f0a0d0a0b08001a0780897a8092f401", writer.toString())
    }

    @Test
    void executeWktToGeoJsonLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "geojson",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String expected = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\"," +
                "\"geometry\":{\"type\":\"Point\",\"coordinates\":[1,2]}," +
                "\"properties\":{},\"id\":\"fid-2cacb84e_14b934d23d9_-7fff\"}]}"
        String actual = writer.toString()
        assertTrue actual.startsWith(expected.substring(0, expected.indexOf("id")))
    }

    @Test
    void executeWktToGmlLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gml",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String expected = "<wfs:FeatureCollection xmlns:gsf=\"http://geoscript.org/feature\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\">" + NEW_LINE +
                "<gml:boundedBy>" + NEW_LINE +
                "<gml:Box>" + NEW_LINE +
                "<gml:coord>" + NEW_LINE +
                "<gml:X>1.0</gml:X>" + NEW_LINE +
                "<gml:Y>2.0</gml:Y>" + NEW_LINE +
                "</gml:coord>" + NEW_LINE +
                "<gml:coord>" + NEW_LINE +
                "<gml:X>1.0</gml:X>" + NEW_LINE +
                "<gml:Y>2.0</gml:Y>" + NEW_LINE +
                "</gml:coord>" + NEW_LINE +
                "</gml:Box>" + NEW_LINE +
                "</gml:boundedBy>" + NEW_LINE +
                "<gml:featureMember>" + NEW_LINE +
                "<gsf:feature fid=\"fid-2cacb84e_14b934d23d9_-7ffe\">" + NEW_LINE +
                "<gsf:geom>" + NEW_LINE +
                "<gml:Point>" + NEW_LINE +
                "<gml:coord>" + NEW_LINE +
                "<gml:X>1.0</gml:X>" + NEW_LINE +
                "<gml:Y>2.0</gml:Y>" + NEW_LINE +
                "</gml:coord>" + NEW_LINE +
                "</gml:Point>" + NEW_LINE +
                "</gsf:geom>" + NEW_LINE +
                "</gsf:feature>" + NEW_LINE +
                "</gml:featureMember>" + NEW_LINE +
                "</wfs:FeatureCollection>" + NEW_LINE
        String actual = writer.toString()
        assertTrue actual.startsWith("<wfs:FeatureCollection")
        assertTrue actual.contains(expected.substring(expected.indexOf("<gml:boundedBy>"), expected.indexOf("<gsf:feature fid=\"")))
        assertTrue actual.endsWith(expected.substring(expected.indexOf("<gsf:geom>")))
    }

    @Test
    void executeWktToKmlLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "kml",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String expected = "<kml:kml xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:kml=\"http://earth.google.com/kml/2.1\">" + NEW_LINE +
                "<kml:Document>" + NEW_LINE +
                "<kml:Placemark id=\"fid-2cacb84e_14b934d23d9_-7ffd\">" + NEW_LINE +
                "<kml:Point>" + NEW_LINE +
                "<kml:coordinates>1.0,2.0</kml:coordinates>" + NEW_LINE +
                "</kml:Point>" + NEW_LINE +
                "</kml:Placemark>" + NEW_LINE +
                "</kml:Document>" + NEW_LINE +
                "</kml:kml>" + NEW_LINE
        String actual = writer.toString()
        assertTrue actual.startsWith(expected.substring(0, expected.indexOf("<kml:Placemark id=\"")))
        assertTrue actual.endsWith(expected.substring(expected.indexOf("<kml:Point>")))
    }

    @Test
    void executeWktToGeoRssLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "georss",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String expected ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<feed xmlns:georss=\"http://www.georss.org/georss\" xmlns=\"http://www.w3.org/2005/Atom\">" + NEW_LINE +
                "<title>feature</title>" + NEW_LINE +
                "<subtitle>feature geom: Point</subtitle>" + NEW_LINE +
                "<link>http://geoscript.org/feature</link>" + NEW_LINE +
                "<entry>" + NEW_LINE +
                "<title>fid-2cacb84e_14b934d23d9_-7ffc</title>" + NEW_LINE +
                "<summary>[geom:POINT (1 2)]</summary>" + NEW_LINE +
                "<updated>Mon Feb 16 08:51:23 PST 2015</updated>" + NEW_LINE +
                "<georss:point>2.0 1.0</georss:point>" + NEW_LINE +
                "</entry>" + NEW_LINE +
                "</feed>" + NEW_LINE
        String actual = writer.toString()
        assertTrue actual.startsWith(expected.substring(0, expected.indexOf("<title>")))
        assertTrue actual.endsWith(expected.substring(expected.indexOf("</updated>")))
    }

    @Test
    void executeWktToGpxLayerFromReader() {
        ConvertCommand cmd = new ConvertCommand()
        ConvertOptions options = new ConvertOptions(
                format: "gpx",
                type: "layer"
        )
        StringReader reader = new StringReader("POINT (1 2)")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"geoscript\">" + NEW_LINE +
                "<wpt lat=\"2.0\" lon=\"1.0\">" + NEW_LINE +
                "<name>fid-2cacb84e_14b934d23d9_-7ffb</name>" + NEW_LINE +
                "</wpt>" + NEW_LINE +
                "</gpx>" + NEW_LINE
        String actual = writer.toString()
        assertTrue actual.startsWith(expected.substring(0, expected.indexOf("<name>")))
        assertTrue actual.endsWith(expected.substring(expected.indexOf("</name>")))
    }


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
        String expected = "<kml:Placemark xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:kml=\"http://earth.google.com/kml/2.1\" id=\"1\">" +
                "<kml:Point>" +
                "<kml:coordinates>1.0,2.0</kml:coordinates>" +
                "</kml:Point>" +
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
        assertStringsEqual(stripXmlNS(expected), stripXmlNS(actual), true)
    }
}
