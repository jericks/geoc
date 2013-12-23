package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Shear Features in a Layer.
 * @author Jared Erickson
 */
class ShearCommand extends LayerInOutCommand<ShearOptions>{

    @Override
    String getName() {
        "vector shear"
    }

    @Override
    String getDescription() {
        "Shear Features in a Layer"
    }

    @Override
    ShearOptions getOptions() {
        new ShearOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, ShearOptions options, Reader reader, Writer writer) throws Exception {
        Expression xExpression = Expression.fromCQL(options.x)
        Expression yExpression = Expression.fromCQL(options.y)
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.cursor.collect{Feature f ->
                double x = xExpression.evaluate(f)
                double y = yExpression.evaluate(f)
                f.geom = f.geom.shear(x,y)
                w.add(f)
            }
        }
    }

    static class ShearOptions extends LayerInOutOptions {

        @Option(name="-x", aliases="--x-distance",  usage="The x distance", required = true)
        String x

        @Option(name="-y", aliases="--y-distance",  usage="The y distance", required = true)
        String y

    }
}
