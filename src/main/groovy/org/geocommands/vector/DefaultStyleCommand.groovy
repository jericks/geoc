package org.geocommands.vector

import geoscript.filter.Color
import geoscript.layer.Layer
import geoscript.style.Symbolizer
import geoscript.workspace.Memory
import org.kohsuke.args4j.Option

/**
 * Get a default SLD style for the Layer.
 * @author Jared Erickson
 */
class DefaultStyleCommand extends LayerCommand<DefaultStyleOptions> {

    @Override
    String getName() {
        "vector defaultstyle"
    }

    @Override
    String getDescription() {
        "Get the default style for the Layer"
    }

    @Override
    DefaultStyleOptions getOptions() {
        new DefaultStyleOptions()
    }

    @Override
    void execute(DefaultStyleOptions options, Reader reader, Writer writer) throws Exception {
        if (options.geometryType) {
            processLayer(null, options, reader, writer)
        } else {
            super.execute(options, reader, writer)
        }
    }

    @Override
    protected void processLayer(Layer layer, DefaultStyleOptions options, Reader reader, Writer writer) throws Exception {
        String sld = Symbolizer.getDefault([
                color: options.color ? new Color(options.color) : "#f2f2f2",
                opacity: options.opacity
        ], options.geometryType ?: layer.schema.geom.typ).sld
        writer.write(sld.trim())
    }

    static class DefaultStyleOptions extends LayerOptions {

        @Option(name = "-g", aliases = "--geometry-type", usage = "The geometry type", required = false)
        String geometryType


        @Option(name = "-c", aliases = "--color", usage = "The base color", required = false)
        String color

        @Option(name = "-o", aliases = "--opacity", usage = "The opacity (defaults to 1.0)", required = false)
        double opacity = 1.0

    }
}
