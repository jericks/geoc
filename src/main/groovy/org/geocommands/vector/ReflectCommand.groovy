package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Reflect Features in a Layer.
 * @author Jared Erickson
 */
class ReflectCommand extends LayerInOutCommand<ReflectOptions>{

    @Override
    String getName() {
        "vector reflect"
    }

    @Override
    String getDescription() {
        "Reflect Features in a Layer"
    }

    @Override
    ReflectOptions getOptions() {
        new ReflectOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, ReflectOptions options, Reader reader, Writer writer) throws Exception {
        Expression x1Expression = Expression.fromCQL(options.x1)
        Expression y1Expression = Expression.fromCQL(options.y1)
        Expression x2Expression = options.x2 ? Expression.fromCQL(options.x2) : null
        Expression y2Expression = options.y2 ? Expression.fromCQL(options.y2) : null
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.cursor.collect{Feature f ->
                double x1 = x1Expression.evaluate(f) as double
                double y1 = y1Expression.evaluate(f) as double
                if (x2Expression && y2Expression) {
                    double x2 = x2Expression.evaluate(f) as double
                    double y2 = y2Expression.evaluate(f) as double
                    f.geom = f.geom.reflect(x1, y1, x2, y2)
                } else {
                    f.geom = f.geom.reflect(x1, y1)
                }
                w.add(f)
            }
        }
    }

    static class ReflectOptions extends LayerInOutOptions {

        @Option(name="-x", aliases="--x1-distance",  usage="The x1 distance", required = true)
        String x1

        @Option(name="-y", aliases="--y1-distance",  usage="The y1 distance", required = true)
        String y1

        @Option(name="-c", aliases="--x2-distance",  usage="The x2 distance", required = false)
        String x2

        @Option(name="-d", aliases="--y2-distance",  usage="The y2 distance", required = false)
        String y2

    }
}
