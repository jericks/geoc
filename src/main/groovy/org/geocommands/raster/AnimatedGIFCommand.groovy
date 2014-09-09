package org.geocommands.raster

import geoscript.render.GIF
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

import javax.imageio.ImageIO

/**
 * Create an animated GIF from a list of GIFs.
 * @author Jared Erickson
 */
class AnimatedGIFCommand extends Command<AnimatedGIFOptions> {

    @Override
    String getName() {
        "raster animatedgif"
    }

    @Override
    String getDescription() {
        "Create an animated GIF from a list of GIFs."
    }

    @Override
    AnimatedGIFOptions getOptions() {
        new AnimatedGIFOptions()
    }

    @Override
    void execute(AnimatedGIFOptions options, Reader reader, Writer writer) throws Exception {
        GIF gif = new GIF()
        gif.renderAnimated(options.files.collect { ImageIO.read(it) }, options.file, options.delay, options.repeat)
    }

    static class AnimatedGIFOptions extends Options {
        @Option(name = "-f", aliases = "--file", usage = "The GIF file", required = true)
        List<File> files

        @Option(name = "-o", aliases = "--output-file", usage = "The output animated GIF file", required = false)
        File file = new File("animated.gif")

        @Option(name = "-d", aliases = "--delay", usage = "The delay between images", required = false)
        int delay = 300

        @Option(name = "-r", aliases = "--repeat", usage = "Whether to repeat the animation or not", required = false)
        boolean repeat
    }

}
