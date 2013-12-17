package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.geom.Point
import geoscript.proj.Geodetic
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Calculate the orthodromic distance between two points.
 * @author Jared Erickson
 */
class OrthodromicDistanceCommand extends Command<OrthodromicDistanceOptions> {

    String getName() {
        "geometry orthodromicdistance"
    }

    String getDescription() {
        "Calculate the orthodromic distance between two points."
    }

    OrthodromicDistanceOptions getOptions() {
        new OrthodromicDistanceOptions()
    }

    void execute(OrthodromicDistanceOptions options, Reader reader, Writer writer) throws Exception {
        Point point1 = Geometry.fromString(options.point1 ?: reader.text).centroid
        Point point2 = Geometry.fromString(options.point2).centroid
        Geodetic geod = new Geodetic(options.ellipsoid)
        Map result = geod.inverse(point1, point2)
        writer.write("${result.distance}")
    }

    static class OrthodromicDistanceOptions extends Options {

        @Option(name="-e", aliases="--ellipsoid",  usage="The ellipsoid", required = false)
        String ellipsoid = "wgs84"

        @Option(name="-p", aliases="--point1",  usage="The first point", required = false)
        String point1

        @Option(name="-t", aliases="--point2",  usage="The second point", required = true)
        String point2

    }
}
