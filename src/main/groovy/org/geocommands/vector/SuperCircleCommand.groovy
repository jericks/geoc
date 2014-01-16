package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Create a super circle shape around each feature of the input Layer
 * @author Jared Erickson
 */
class SuperCircleCommand extends ShapeCommand<SuperCircleOptions> {

    @Override
    String getName() {
        "vector supercircle"
    }

    @Override
    String getDescription() {
        "Create a super circle shape around each feature of the input Layer"
    }

    @Override
    SuperCircleOptions getOptions() {
        new SuperCircleOptions()
    }

    @Override
    Geometry createShape(SuperCircleOptions options, Bounds bounds) {
        double rotation = getAngle(options, options.rotation)
        bounds.createSuperCircle(options.power, options.numPoints, rotation)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, SuperCircleOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "supercirlces", options))
    }

    static class SuperCircleOptions extends ShapeOptions {

        @Option(name = "-e", aliases = "--power", usage = "The power", required = true)
        double power

    }
}
