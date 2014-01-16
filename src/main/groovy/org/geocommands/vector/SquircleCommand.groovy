package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Layer

/**
 * Create a squircle shape around each feature of the input Layer
 * @author Jared Erickson
 */
class SquircleCommand extends ShapeCommand<SquircleOptions> {

    @Override
    String getName() {
        "vector squircle"
    }

    @Override
    String getDescription() {
        "Create a squircle shape around each feature of the input Layer"
    }

    @Override
    SquircleOptions getOptions() {
        new SquircleOptions()
    }

    @Override
    Geometry createShape(SquircleOptions options, Bounds bounds) {
        double rotation = getAngle(options, options.rotation)
        bounds.createSquircle(options.numPoints, rotation)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, SquircleOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "squircle", options))
    }

    static class SquircleOptions extends ShapeOptions {

    }
}
