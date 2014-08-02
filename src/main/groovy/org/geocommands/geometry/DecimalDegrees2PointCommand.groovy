package org.geocommands.geometry

import geoscript.geom.Point
import geoscript.proj.DecimalDegrees
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert a decimal degrees formatted string into a Point
 * @author Jared Erickson
 */
class DecimalDegrees2PointCommand extends Command<DecimalDegrees2PointOptions> {

    String getName() {
        "geometry dd2pt"
    }

    String getDescription() {
        "Convert a decimal degrees formatted string into a Point"
    }

    DecimalDegrees2PointOptions getOptions() {
        new DecimalDegrees2PointOptions()
    }

    void execute(DecimalDegrees2PointOptions options, Reader reader, Writer writer) throws Exception {
        DecimalDegrees dd = new DecimalDegrees(options.decimalDegrees ?: reader.text.trim())
        Point pt = dd.point
        if (options.outputType.equalsIgnoreCase("xy")) {
            writer.write("${pt.x}, ${pt.y}")
        } else if (options.outputType.equalsIgnoreCase("wkt")) {
            writer.write(pt.wkt)
        } else if (options.outputType.equalsIgnoreCase("json")) {
            writer.write(pt.geoJSON)
        } else {
            writer.write(pt.wkt)
        }
    }

    static class DecimalDegrees2PointOptions extends Options {

        @Option(name = "-d", aliases = "--decimaldegrees", usage = "The decimal degrees", required = false)
        String decimalDegrees

        @Option(name = "-t", aliases = "--type", usage = "The output type", required = false)
        String outputType = "XY"

    }
}
