package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Create a arc shape around each feature of the input Layer
 * @author Jared Erickson
 */
class ArcCommand extends ShapeCommand<ArcOptions> {

    @Override
    String getName() {
        "vector arc"
    }

    @Override
    String getDescription() {
        "Create a arc shape around each feature of the input Layer"
    }

    @Override
    ArcOptions getOptions() {
        new ArcOptions()
    }

    @Override
    Geometry createShape(ArcOptions options, Bounds bounds) {
        double startAngle = getAngle(options, options.startAngle)
        double endAngle = getAngle(options, options.endAngle)
        double rotation = getAngle(options, options.rotation)
        bounds.createArc(startAngle, endAngle, options.numPoints, rotation)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, ArcOptions options) {
        layer.schema.changeGeometryType("LineString", getOutputLayerName(layer, "arc", options))
    }

    static class ArcOptions extends ShapeOptions {

        @Option(name = "-s", aliases = "--start-angle", usage = "The start angle", required = true)
        double startAngle

        @Option(name = "-e", aliases = "--end-angle", usage = "The end angle", required = true)
        double endAngle

    }
}
