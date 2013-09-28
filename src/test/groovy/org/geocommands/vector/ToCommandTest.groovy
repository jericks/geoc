package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.ToCommand.ToOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The ToCommand Unit Test
 * @author Jared Erickson
 */
class ToCommandTest extends BaseTest {

    @Test void executeFromPropertyFileToGeoJson() {
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


    @Test void executeFromCsvToKml() {
        ToCommand cmd = new ToCommand()
        ToOptions options = new ToOptions(
            format: "kml"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String kml = writer.toString()
        assertTrue(kml.startsWith("<kml:kml"))
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.properties")
        String output = runApp([
            "vector to",
            "-i", file.absolutePath,
            "-f", "csv"
        ],"")
        String actual = output
        String expected = """"the_geom:Point","distance:String","name:String"
"POINT (1 1)","2","Number 1"
"POINT (10 10)","1","Number 2"
"POINT (2 8)","5","Number 3"
"""
        assertEquals(expected, actual)

        output = runApp([
            "vector to",
            "-f", "gml"
        ],readCsv("points.csv").text)
        String gml = output
        assertTrue(gml.startsWith("<wfs:FeatureCollection"))
    }
}
