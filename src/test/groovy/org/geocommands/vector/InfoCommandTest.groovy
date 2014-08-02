package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.InfoCommand.InfoOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The InfoCommand Unit Test
 * @author Jared Erickson
 */
class InfoCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points.properties")
        InfoCommand cmd = new InfoCommand()
        InfoOptions options = new InfoOptions(inputWorkspace: file.getAbsolutePath())
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals """Name: points
Geometry: Point
Extent: 1.0, 1.0, 10.0, 10.0
Projection ID: Unknown
Projection WKT: Unknown
Feature Count: 3
Fields:
the_geom: Point
distance: String
name: String""", w.toString()

    }

    @Test
    void executeFromCsv() {
        InfoCommand cmd = new InfoCommand()
        InfoOptions options = new InfoOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        assertEquals """Name: csv
Geometry: Point
Extent: 1.0, 1.0, 10.0, 10.0
Projection ID: Unknown
Projection WKT: Unknown
Feature Count: 3
Fields:
the_geom: Point
distance: String
name: String""", w.toString()

    }

    @Test
    void runAsCommandLine() {
        String str = runApp(["vector info"], readCsv("points.csv").text)
        assertEquals """Name: csv
Geometry: Point
Extent: 1.0, 1.0, 10.0, 10.0
Projection ID: Unknown
Projection WKT: Unknown
Feature Count: 3
Fields:
the_geom: Point
distance: String
name: String
""", str

        File file = getResource("points.properties")
        str = runApp(["vector info", "-i", file.absolutePath], "")
        assertEquals """Name: points
Geometry: Point
Extent: 1.0, 1.0, 10.0, 10.0
Projection ID: Unknown
Projection WKT: Unknown
Feature Count: 3
Fields:
the_geom: Point
distance: String
name: String
""", str
    }

}
