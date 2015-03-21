package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.ToCommand.ToOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The ToCommand Unit Test
 * @author Jared Erickson
 */
class ToCommandTest extends BaseTest {

    @Test
    void executeFromPropertyFileToGeoJson() {
        ToCommand cmd = new ToCommand()
        File file = getResource("points.properties")
        ToOptions options = new ToOptions(
                inputWorkspace: file.absolutePath,
                format: "geojson"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[1,1]},"properties":{"distance":"2","name":"Number 1"},"id":"points.1359223728143"},{"type":"Feature","geometry":{"type":"Point","coordinates":[10,10]},"properties":{"distance":"1","name":"Number 2"},"id":"points.1359223728171"},{"type":"Feature","geometry":{"type":"Point","coordinates":[2,8]},"properties":{"distance":"5","name":"Number 3"},"id":"points.1359223728252"}]}"""
        assertEquals(expected, actual)
    }


    @Test
    void executeFromCsvToKml() {
        ToCommand cmd = new ToCommand()
        ToOptions options = new ToOptions(
                format: "kml"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String kml = writer.toString()
        assertTrue(kml.startsWith("<kml:kml"))
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        String output = runApp([
                "vector to",
                "-i", file.absolutePath,
                "-f", "csv"
        ], "")
        String actual = output
        String expected = """"the_geom:Point","distance:String","name:String"
"POINT (1 1)","2","Number 1"
"POINT (10 10)","1","Number 2"
"POINT (2 8)","5","Number 3"
"""
        assertStringsEqual(expected, actual)

        output = runApp([
                "vector to",
                "-f", "gml"
        ], readCsv("points.csv").text)
        String gml = output
        assertTrue(gml.startsWith("<wfs:FeatureCollection"))
    }

    @Test void runCsvXYColumns() {
        String actual = runApp([
                "vector to",
                "-f", "csv",
                "-p", "type=XY",
                "-p", "xColumn=x",
                "-p", "yColumn=y"
        ], readCsv("points.csv").text)
        String expected = """"x","y","distance","name"
"1.0","1.0","2","Number 1"
"10.0","10.0","1","Number 2"
"2.0","8.0","5","Number 3"
"""
        assertStringsEqual(expected, actual)
    }

    @Test void runCsvXYColumn() {
        String actual = runApp([
                "vector to",
                "-f", "csv",
                "-p", "type=XY",
                "-p", "separator=|",
                "-p", "encodeFieldType=true"
        ], readCsv("points.csv").text)
        String expected = """"the_geom:Point"|"distance:String"|"name:String"
"1.0,1.0"|"2"|"Number 1"
"10.0,10.0"|"1"|"Number 2"
"2.0,8.0"|"5"|"Number 3"
"""
        assertStringsEqual(expected, actual)
    }

    @Test void runGeoRSSAtom() {
        String actual = runApp([
                "vector to",
                "-f", "georss"
        ], readCsv("points.csv").text)
        assertTrue actual.contains("<feed xmlns:georss=\"http://www.georss.org/georss\" xmlns=\"http://www.w3.org/2005/Atom\">")
    }

    @Test void runGeoRSS() {
        String actual = runApp([
                "vector to",
                "-f", "georss",
                "-p", "feedType=rss",
                "-p", "itemTitle=f['name']",
                "-p", "itemDescription=f['name'] + ' is ' + f['distance'] + ' miles away'",
                "-p", "itemDate='11/14/1980'"
        ], readCsv("points.csv").text)
        assertTrue actual.contains("<rss xmlns:georss=\"http://www.georss.org/georss\" version=\"2.0\">")
    }

    @Test void runGml() {
        String actual = runApp([
                "vector to",
                "-f", "gml",
                "-p", "version=3",
                "-p", "format=false"
        ], readCsv("points.csv").text)
        assertTrue actual.startsWith("<wfs:FeatureCollection")
    }

    @Test void runGpx() {
        String actual = runApp([
                "vector to",
                "-f", "gpx",
                "-p", "version=1.1",
                "-p", "includeAttributes=true",
                "-p", "name=f['name']"
        ], readCsv("points.csv").text)
        assertTrue stripXmlNS(actual).contains("<gpx   version=\"1.1\" creator=\"geoscript\">")
    }
}
