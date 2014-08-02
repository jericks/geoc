package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Scale Features in a Layer.
 * @author Jared Erickson
 */
class ScaleCommand extends LayerInOutCommand<ScaleOptions> {

    @Override
    String getName() {
        "vector scale"
    }

    @Override
    String getDescription() {
        "Scale Feature in a Layer"
    }

    @Override
    ScaleOptions getOptions() {
        new ScaleOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, ScaleOptions options, Reader reader, Writer writer) throws Exception {
        Expression xDistanceExpression = Expression.fromCQL(options.xDistance)
        Expression yDistanceExpression = Expression.fromCQL(options.yDistance)
        Expression xCoordExpression = options.xCoord ? Expression.fromCQL(options.xCoord) : null
        Expression yCoordExpression = options.yCoord ? Expression.fromCQL(options.yCoord) : null
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.cursor.collect { Feature f ->
                double xDistance = xDistanceExpression.evaluate(f) as double
                double yDistance = yDistanceExpression.evaluate(f) as double
                if (xCoordExpression && yCoordExpression) {
                    double xCoord = xCoordExpression.evaluate(f) as double
                    double yCoord = yCoordExpression.evaluate(f) as double
                    f.geom = f.geom.scale(xDistance, yDistance, xCoord, yCoord)
                } else {
                    f.geom = f.geom.scale(xDistance, yDistance)
                }
                w.add(f)
            }
        }
    }

    static class ScaleOptions extends LayerInOutOptions {

        @Option(name = "-x", aliases = "--x-distance", usage = "The x distance", required = true)
        String xDistance

        @Option(name = "-y", aliases = "--y-distance", usage = "The y distance", required = true)
        String yDistance

        @Option(name = "-c", aliases = "--x-coord", usage = "The x coordinate", required = false)
        String xCoord

        @Option(name = "-d", aliases = "--y-coord", usage = "The y coordinate", required = false)
        String yCoord

    }
}
