package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.geom.Point
import geoscript.proj.DecimalDegrees
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Format a Point in Decimal Degrees.
 * @author Jared Erickson
 */
class Point2DecimalDegreesCommand extends Command<Point2DecimalDegreesOptions> {

    String getName() {
        "geometry pt2dd"
    }

    String getDescription() {
        "Format a Point in Decimal Degrees"
    }

    Point2DecimalDegreesOptions getOptions() {
        new Point2DecimalDegreesOptions()
    }

    void execute(Point2DecimalDegreesOptions options, Reader reader, Writer writer) throws Exception {
        Point pt = Geometry.fromString(options.point ?: reader.text).centroid
        DecimalDegrees dd = new DecimalDegrees(pt)
        if (options.outputType.equalsIgnoreCase("dms")) {
            writer.write(dd.toDms())
        } else if (options.outputType.equalsIgnoreCase("dms_char")) {
            writer.write(dd.toDms(false))
        } else if (options.outputType.equalsIgnoreCase("ddm")) {
            writer.write(dd.toDdm())
        } else if (options.outputType.equalsIgnoreCase("ddm_char")) {
            writer.write(dd.toDdm(false))
        } else {
            writer.write(dd.toDms())
        }
    }

    static class Point2DecimalDegreesOptions extends Options {

        @Option(name = "-p", aliases = "--point", usage = "The Point", required = false)
        String point

        @Option(name = "-t", aliases = "--type", usage = "The output type (dms, dms_char, ddm, ddm_char)", required = false)
        String outputType = "dms"
    }
}
