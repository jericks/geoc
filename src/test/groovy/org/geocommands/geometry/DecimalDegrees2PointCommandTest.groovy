package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.geom.Point
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The DecimalDegrees2PointCommand Unit Test
 * @author Jared Erickson
 */
class DecimalDegrees2PointCommandTest extends BaseTest {

    @Test void execute() {
        DecimalDegrees2PointCommand cmd = new DecimalDegrees2PointCommand()
        DecimalDegrees2PointCommand.DecimalDegrees2PointOptions options = new DecimalDegrees2PointCommand.DecimalDegrees2PointOptions(
            decimalDegrees: """122\u00B0 31' 32.23\" W, 47\u00B0 12' 43.28\" N""",
            outputType: "xy"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        List results = writer.toString().split(",")
        assertEquals(-122.5256194, results[0] as double, 0.001)
        assertEquals(47.212022222, results[1] as double, 0.001)

        options = new DecimalDegrees2PointCommand.DecimalDegrees2PointOptions(outputType: "wkt")
        writer = new StringWriter()
        cmd.execute(options, new StringReader("""122\u00B0 31' 32.23\" W, 47\u00B0 12' 43.28\" N"""), writer)
        Point point = Geometry.fromWKT(writer.toString())
        assertEquals(-122.5256194, point.x, 0.001)
        assertEquals(47.212022222, point.y, 0.001)
    }

    @Test void runAsCommandLine() {
        String result = runApp(["geometry dd2pt", "-d", """122\u00B0 31' 32.23\" W, 47\u00B0 12' 43.28\" N"""],"")
        List results = result.split(",")
        assertEquals(-122.5256194, results[0] as double, 0.001)
        assertEquals(47.212022222, results[1] as double, 0.001)

        result = runApp(["geometry dd2pt", "-t", "wkt"], "122d 31m 32.23s W, 47d 12m 43.28s N")
        Point point = Geometry.fromWKT(result)
        assertEquals(-122.5256194, point.x, 0.001)
        assertEquals(47.212022222, point.y, 0.001)
    }

}
