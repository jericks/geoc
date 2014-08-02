package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.style.Gradient
import org.kohsuke.args4j.Option

/**
 * Create a gradient SLD for the Layer.
 * @author Jared Erickson
 */
class GradientStyleCommand extends LayerCommand<GradientStyleOptions> {

    @Override
    String getName() {
        "vector gradientstyle"
    }

    @Override
    String getDescription() {
        "Create a gradient SLD for the Layer."
    }

    @Override
    GradientStyleOptions getOptions() {
        new GradientStyleOptions()
    }

    @Override
    protected void processLayer(Layer layer, GradientStyleOptions options, Reader reader, Writer writer) throws Exception {
        def colors
        if (options.colors.split(" ").length > 1) {
            colors = []
            colors.addAll(options.colors.split(" "))
        } else {
            colors = options.colors as String
        }
        Gradient gradient = new Gradient(layer, options.field, options.method, options.number, colors, options.elseMode)
        writer.write(gradient.sld.trim())
    }

    static class GradientStyleOptions extends LayerOptions {

        @Option(name = "-f", aliases = "--field", usage = "The field name", required = true)
        String field

        @Option(name = "-n", aliases = "--number", usage = "The number of categories", required = true)
        int number

        @Option(name = "-c", aliases = "--colors", usage = "The color brewer palette name or a list of colors (space delimited)", required = true)
        String colors

        @Option(name = "-m", aliases = "--method", usage = "The classification method (Quantile or EqualInterval)", required = false)
        String method = "Quantile"

        @Option(name = "-e", aliases = "--else-mode", usage = "The else mode (ignore, min, max)", required = false)
        String elseMode = "ignore"

    }
}
