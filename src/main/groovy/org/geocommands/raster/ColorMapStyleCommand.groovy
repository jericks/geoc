package org.geocommands.raster

import geoscript.filter.Color
import geoscript.style.ColorMap
import org.geocommands.Command
import org.kohsuke.args4j.Option

/**
 * Create a color map Raster style
 * @author Jared Erickson
 */
class ColorMapStyleCommand extends Command<ColorMapStyleOptions> {

    @Override
    String getName() {
        "raster style colormap"
    }

    @Override
    String getDescription() {
        "Create a color map Raster style"
    }

    @Override
    ColorMapStyleOptions getOptions() {
        new ColorMapStyleOptions()
    }

    @Override
    void execute(ColorMapStyleOptions options, Reader reader, Writer writer) throws Exception {
        ColorMap style = new ColorMap(options.values.collect { String v ->
            List parts = v.split("=") as List
            [quantity: parts[0], color: new Color(parts[1]).hex]
        }, options.type, options.extended)
        style.opacity = options.opacity
        writer.write(style.sld)
    }

    static class ColorMapStyleOptions extends StyleOptions {

        @Option(name = "-v", aliases = "--value", usage = "A value", required = true)
        List<String> values

        @Option(name = "-t", aliases = "--type", usage = "The type (intervals, values, ramp)", required = false)
        String type = "ramp"

        @Option(name = "-e", aliases = "--extended", usage = "Whether to use extended colors or not", required = false)
        boolean extended = false

    }
}
