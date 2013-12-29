package org.geocommands.vector

import org.kohsuke.args4j.Option

/**
 * Created by jericks on 12/27/13.
 */
class ShapeOptions extends LayerInOutOptions {

    @Option(name="-g", aliases="--geometry",  usage="The geometry expression", required = false)
    String geometry

    @Option(name="-w", aliases="--width",  usage="The width of the bounds", required = false)
    double width

    @Option(name="-h", aliases="--height",  usage="The height of the bounds", required = false)
    double height

    @Option(name="-p", aliases="--num-points",  usage="The number of points", required = false)
    int numPoints = 20

    @Option(name="-a", aliases="--rotation",  usage="The angle of rotation", required = false)
    double rotation = 0

    @Option(name="-u", aliases="--unit",  usage="The unit can either be degrees(d) or radians(r). The default is degrees.", required = false)
    String unit = "degrees"
}
