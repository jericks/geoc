package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.geom.LineString
import geoscript.geom.Point
import geoscript.proj.Geodetic
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Create a great circle arc.
 * @author Jared Erickson
 */
class GreatCircleArcCommand extends Command<GreatCircleArcCommandOptions> {

    String getName() {
        "geometry greatcirclearc"
    }

    String getDescription() {
        "Calculate the orthodromic distance between two points."
    }

    GreatCircleArcCommandOptions getOptions() {
        new GreatCircleArcCommandOptions()
    }

    void execute(GreatCircleArcCommandOptions options, Reader reader, Writer writer) throws Exception {
        Point startPoint = Geometry.fromString(options.startPoint ?: reader.text).centroid
        Point endPoint = Geometry.fromString(options.endPoint).centroid
        Geodetic geod = new Geodetic(options.ellipsoid)
        LineString line = new LineString(geod.placePoints(startPoint, endPoint, options.numberOfPoints))
        writer.write(line.wkt)
    }

    static class GreatCircleArcCommandOptions extends Options {

        @Option(name="-e", aliases="--ellipsoid",  usage="The ellipsoid", required = false)
        String ellipsoid = "wgs84"

        @Option(name="-p", aliases="--start-point",  usage="The start point", required = false)
        String startPoint

        @Option(name="-t", aliases="--end-point",  usage="The end point", required = true)
        String endPoint

        @Option(name="-n", aliases="--num-points",  usage="The number of points", required = false)
        int numberOfPoints = 100

    }
}
