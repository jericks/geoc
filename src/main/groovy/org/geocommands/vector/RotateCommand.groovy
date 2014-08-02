package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Rotate Features in a Layer.
 * @author Jared Erickson
 */
class RotateCommand extends LayerInOutCommand<RotateOptions> {

    @Override
    String getName() {
        "vector rotate"
    }

    @Override
    String getDescription() {
        "Rotate Features in a Layer"
    }

    @Override
    RotateOptions getOptions() {
        new RotateOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, RotateOptions options, Reader reader, Writer writer) throws Exception {
        Closure c
        // theta
        if (options.theta) {
            // theta x y
            if (options.x && options.y) {
                Expression thetaExpression = Expression.fromCQL(options.theta)
                Expression xExpression = Expression.fromCQL(options.x)
                Expression yExpression = Expression.fromCQL(options.y)
                c = { geoscript.layer.Writer w, Feature f ->
                    double theta = thetaExpression.evaluate(f)
                    double x = xExpression.evaluate(f)
                    double y = yExpression.evaluate(f)
                    f.geom = f.geom.rotate(theta, x, y)
                    w.add(f)
                }
            }
            // theta
            else {
                Expression thetaExpression = Expression.fromCQL(options.theta)
                c = { geoscript.layer.Writer w, Feature f ->
                    double theta = thetaExpression.evaluate(f)
                    f.geom = f.geom.rotate(theta)
                    w.add(f)
                }
            }
        }
        // sin and cos
        else if (options.sin && options.cos) {
            // sin cos x y
            if (options.x && options.y) {
                Expression sinExpression = Expression.fromCQL(options.sin)
                Expression cosExpression = Expression.fromCQL(options.cos)
                Expression xExpression = Expression.fromCQL(options.x)
                Expression yExpression = Expression.fromCQL(options.y)
                c = { geoscript.layer.Writer w, Feature f ->
                    double sin = sinExpression.evaluate(f)
                    double cos = cosExpression.evaluate(f)
                    double x = xExpression.evaluate(f)
                    double y = yExpression.evaluate(f)
                    f.geom = f.geom.rotate(sin, cos, x, y)
                    w.add(f)
                }
            }
            // sin and cos
            else {
                Expression sinExpression = Expression.fromCQL(options.sin)
                Expression cosExpression = Expression.fromCQL(options.cos)
                c = { geoscript.layer.Writer w, Feature f ->
                    double sin = sinExpression.evaluate(f)
                    double cos = cosExpression.evaluate(f)
                    f.geom = f.geom.rotate(sin, cos)
                    w.add(f)
                }
            }
        } else {
            throw new IllegalArgumentException("Illegal combination of arguments! theta or theta, x, y or sin,cos,x,y or sin,cos.")
        }

        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.cursor.collect { Feature f ->
                c.call(w, f)
            }
        }
    }

    static class RotateOptions extends LayerInOutOptions {

        @Option(name = "-t", aliases = "--theta", usage = "The angle of rotation in radians", required = false)
        String theta

        @Option(name = "-s", aliases = "--sine", usage = "The sine of the angle of rotation in radians", required = false)
        String sin

        @Option(name = "-c", aliases = "--cosine", usage = "The cosine of the angle of rotation in radians", required = false)
        String cos

        @Option(name = "-x", aliases = "--x-coord", usage = "The x coordinate of the rotation point", required = false)
        String x

        @Option(name = "-y", aliases = "--y-coord", usage = "The y coordinate of the rotation point", required = false)
        String y

    }
}
