package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.filter.Expression
import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.geom.Point
import geoscript.layer.Layer

/**
 * Create a shape around the current features of the input Layer
 * @author Jared Erickson
 */
abstract class ShapeCommand<T extends ShapeOptions> extends LayerInOutCommand<T> {

    abstract Geometry createShape(T options, Bounds bounds)

    protected double getAngle(T options, double angle) {
        if (options.unit.equalsIgnoreCase("degrees") || options.unit.equalsIgnoreCase("d")) {
            org.locationtech.jts.algorithm.Angle.toRadians(angle)
        } else {
            angle
        }
    }

    protected double getAngle(T options) {
        getAngle(options, options.rotation)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, T options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "shape", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, T options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Geometry geom = options.geometry ? Expression.fromCQL(options.geometry).evaluate(f) : f.geom
                Bounds bounds
                if (options.width && options.height) {
                    Point pt = geom.centroid
                    bounds = new Bounds(
                            pt.x - options.width / 2,
                            pt.y - options.height / 2,
                            pt.x + options.width / 2,
                            pt.y + options.height / 2
                    )
                } else {
                    bounds = geom.bounds.ensureWidthAndHeight()
                }
                Geometry newGeom = createShape(options, bounds)
                f.geom = newGeom
                w.add(f)
            }
        }
    }
}
