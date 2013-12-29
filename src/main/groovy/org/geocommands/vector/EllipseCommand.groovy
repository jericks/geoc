package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry

/**
 * Create a ellipse shape around each feature of the input Layer
 * @author Jared Erickson
 */
class EllipseCommand extends ShapeCommand<EllipseOptions> {

    @Override
    String getName() {
        "vector ellipse"
    }

    @Override
    String getDescription() {
        "Create a ellipse shape around each feature of the input Layer"
    }

    @Override
    EllipseOptions getOptions() {
        new EllipseOptions()
    }

    @Override
    Geometry createShape(EllipseOptions options, Bounds bounds) {
        bounds.createEllipse(options.numPoints, getAngle(options, options.rotation))
    }

    static class EllipseOptions extends ShapeOptions {
    }
}
