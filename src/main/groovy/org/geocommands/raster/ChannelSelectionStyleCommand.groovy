package org.geocommands.raster

import geoscript.style.ChannelSelection
import geoscript.style.ContrastEnhancement
import org.geocommands.Command
import org.kohsuke.args4j.Option

/**
 * Create a channel selection Raster SLD
 * @author Jared Erickson
 */
class ChannelSelectionStyleCommand extends Command<ChannelSelectionStyleOptions> {

    @Override
    String getName() {
        "raster style channel selection"
    }

    @Override
    String getDescription() {
        "Create a channel selection Raster SLD"
    }

    @Override
    ChannelSelectionStyleOptions getOptions() {
        new ChannelSelectionStyleOptions()
    }

    @Override
    void execute(ChannelSelectionStyleOptions options, Reader reader, Writer writer) throws Exception {
        ChannelSelection style = new ChannelSelection()
        if (options.gray) {
            Map grayMap = parseString(options.gray)
            style.gray(grayMap.name, grayMap.contrastEnhancement)
        } else {
            Map redMap = parseString(options.red)
            style.red(redMap.name, redMap.contrastEnhancement)
            Map greenMap = parseString(options.green)
            style.green(greenMap.name, greenMap.contrastEnhancement)
            Map blueMap = parseString(options.blue)
            style.blue(blueMap.name, blueMap.contrastEnhancement)
        }
        style.opacity = options.opacity
        writer.write(style.sld)
    }

    /**
     * Parse a string that can either have the channel name or
     * the name,method,value
     */
    private Map parseString(String str) {
        def parts = str.split(",")
        Map map = [name: parts[0], contrastEnhancement: null]
        if (parts.length > 1) {
            String method = parts[1]
            String gamma = parts.length > 2 ? parts[2] : null
            map.contrastEnhancement = new ContrastEnhancement(method, gamma)
        }
        map
    }

    static class ChannelSelectionStyleOptions extends StyleOptions {

        @Option(name = "-r", aliases = "--red", usage = "The red channel name", required = false)
        String red

        @Option(name = "-g", aliases = "--green", usage = "The green channel name", required = false)
        String green

        @Option(name = "-b", aliases = "--blue", usage = "The blue channel name", required = false)
        String blue

        @Option(name = "-y", aliases = "--gray", usage = "The gray channel name", required = false)
        String gray
    }

}
