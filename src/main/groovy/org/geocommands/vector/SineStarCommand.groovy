package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import org.kohsuke.args4j.Option

/**
 * Create a sinestar shape around each feature of the input Layer
 * @author Jared Erickson
 */
class SineStarCommand extends ShapeCommand<SineStarOptions> {

    @Override
    String getName() {
        "vector sinestar"
    }

    @Override
    String getDescription() {
        "Create a sinestar shape around each feature of the input Layer"
    }

    @Override
    SineStarOptions getOptions() {
        new SineStarOptions()
    }

    @Override
    Geometry createShape(SineStarOptions options, Bounds bounds) {
        double rotation = getAngle(options, options.rotation)
        bounds.createSineStar(options.numberOfArms, options.armLengthRatio, options.numPoints, rotation)
    }

    static class SineStarOptions extends ShapeOptions {

        @Option(name = "-n", aliases = "--number-of-arms", usage = "The number of arms", required = true)
        int numberOfArms

        @Option(name = "-e", aliases = "--arm-length-ratio", usage = "The arm length ratio", required = true)
        double armLengthRatio

    }
}
