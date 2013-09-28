package org.geocommands.raster

import geoscript.style.ShadedRelief
import org.geocommands.Command
import org.kohsuke.args4j.Option

/**
 * Create a shaded relief Raster SLD
 * @author Jared Erickson
 */
class ShadedReliefStyleCommand extends Command<ShadedReliefStyleOptions> {

    @Override
    String getName() {
        "raster style shadedrelief"
    }

    @Override
    String getDescription() {
        "Create a shaded relief Raster SLD"
    }

    @Override
    ShadedReliefStyleOptions getOptions() {
        new ShadedReliefStyleOptions()
    }

    @Override
    void execute(ShadedReliefStyleOptions options, Reader reader, Writer writer) throws Exception {
        ShadedRelief style = new ShadedRelief(options.reliefFactor, options.brightnessOnly)
        style.opacity = options.opacity
        writer.write(style.sld)
    }

    static class ShadedReliefStyleOptions extends StyleOptions {
        @Option(name = "-b", aliases = "--brightness-only", usage = "The brightness only flag", required = false)
        boolean brightnessOnly = false

        @Option(name = "-r", aliases = "--relief-factor", usage = "The relief factor", required = false)
        String reliefFactor = "55"
    }
}
