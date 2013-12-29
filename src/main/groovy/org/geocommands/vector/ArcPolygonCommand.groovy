package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import org.kohsuke.args4j.Option

/**
 * Create a arc polygon shape around each feature of the input Layer
 * @author Jared Erickson
 */
class ArcPolygonCommand extends ShapeCommand<ArcPolygonOptions> {

    @Override
    String getName() {
        "vector arcpolygon"
    }

    @Override
    String getDescription() {
        "Create a arc polygon shape around each feature of the input Layer"
    }

    @Override
    ArcPolygonOptions getOptions() {
        new ArcPolygonOptions()
    }

    @Override
    Geometry createShape(ArcPolygonOptions options, Bounds bounds) {
        double startAngle = getAngle(options, options.startAngle)
        double endAngle = getAngle(options, options.endAngle)
        double rotation = getAngle(options, options.rotation)
        bounds.createArcPolygon(startAngle, endAngle, options.numPoints, rotation)
    }

    static class ArcPolygonOptions extends ShapeOptions {

        @Option(name="-s", aliases="--start-angle",  usage="The start angle", required = true)
        double startAngle

        @Option(name="-e", aliases="--end-angle",  usage="The end angle", required = true)
        double endAngle

    }
}
