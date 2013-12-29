package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.geom.Geometry

/**
 * Create a rectangle shape around each feature of the input Layer
 * @author Jared Erickson
 */
class RectangleCommand extends ShapeCommand<RectangleOptions> {

    @Override
    String getName() {
        "vector rectangle"
    }

    @Override
    String getDescription() {
        "Create a rectangle shape around each feature of the input Layer"
    }

    @Override
    RectangleOptions getOptions() {
        new RectangleOptions()
    }

    @Override
    Geometry createShape(RectangleOptions options, Bounds bounds) {
        bounds.createRectangle(options.numPoints, getAngle(options))
    }

    static class RectangleOptions extends ShapeOptions {
    }
}
