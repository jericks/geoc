package org.geocommands

import org.geocommands.Point2DecimalDegreesCommand.Point2DecimalDegreesOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The Point2DecimalDegreesCommand Unit Test
 * @author Jared Erickson
 */
class Point2DecimalDegreesCommandTest extends BaseTest {

    @Test void execute() {
        Point2DecimalDegreesCommand cmd = new Point2DecimalDegreesCommand()
        Point2DecimalDegreesOptions options = new Point2DecimalDegreesOptions(
            point: "POINT (-122.5256194 47.212022222)",
            outputType: "dms"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        assertEquals """-122\u00B0 31' 32.2298\" W, 47\u00B0 12' 43.2800\" N""", writer.toString()

        options = new Point2DecimalDegreesOptions(outputType: "dms_char")
        writer = new StringWriter()
        cmd.execute(options, new StringReader("POINT (-122.5256194 47.212022222)"), writer)
        assertEquals "-122d 31m 32.2298s W, 47d 12m 43.2800s N", writer.toString()
    }

    @Test void runAsCommandLine() {
        String result = runApp(["pt2dd", "-p", "POINT (-122.5256194 47.212022222)", "-t", "ddm"],"")
        assertEquals "-122\u00B0 31.5372' W, 47\u00B0 12.7213' N", result.trim()

        result = runApp(["pt2dd", "-t", "ddm_char"],"POINT (-122.5256194 47.212022222)")
        assertEquals "-122d 31.5372m W, 47d 12.7213m N", result.trim()
    }
}
