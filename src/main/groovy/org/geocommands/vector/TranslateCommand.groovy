package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Translate or move Features in a Layer.
 * @author Jared Erickson
 */
class TranslateCommand extends LayerInOutCommand<TranslateOptions> {

    @Override
    String getName() {
        "vector translate"
    }

    @Override
    String getDescription() {
        "Translate or move Feature in a Layer"
    }

    @Override
    TranslateOptions getOptions() {
        new TranslateOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, TranslateOptions options, Reader reader, Writer writer) throws Exception {
        Expression xExpression = Expression.fromCQL(options.x)
        Expression yExpression = Expression.fromCQL(options.y)
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.cursor.collect { Feature f ->
                double x = xExpression.evaluate(f)
                double y = yExpression.evaluate(f)
                f.geom = f.geom.translate(x, y)
                w.add(f)
            }
        }
    }

    static class TranslateOptions extends LayerInOutOptions {

        @Option(name = "-x", aliases = "--x-distance", usage = "The x distance", required = true)
        String x

        @Option(name = "-y", aliases = "--y-distance", usage = "The y distance", required = true)
        String y

    }
}
