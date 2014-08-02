package org.geocommands.raster

import geoscript.style.ContrastEnhancement
import org.geocommands.Command
import org.kohsuke.args4j.Option

/**
 * Create a contrast enhancement Raster SLD
 * @author Jared Erickson
 */
class ContrastEnhancementStyleCommand extends Command<ContrastEnhancementStyleOptions> {

    @Override
    String getName() {
        "raster style contrast enhancement"
    }

    @Override
    String getDescription() {
        "Create a contrast enhancement Raster SLD"
    }

    @Override
    ContrastEnhancementStyleOptions getOptions() {
        new ContrastEnhancementStyleOptions()
    }

    @Override
    void execute(ContrastEnhancementStyleOptions options, Reader reader, Writer writer) throws Exception {
        ContrastEnhancement style = new ContrastEnhancement(options.method, options.gammaValue)
        style.opacity = options.opacity
        writer.write(style.sld)
    }

    static class ContrastEnhancementStyleOptions extends StyleOptions {

        @Option(name = "-m", aliases = "--method", usage = "The method (normalize or histogram)", required = false)
        String method = "normalize"

        @Option(name = "-g", aliases = "--gamma-value", usage = "The gamma value", required = false)
        String gammaValue

    }
}
