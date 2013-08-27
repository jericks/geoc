package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Draw a Layer to an Image, PDF, SVG Document
 */
class DrawLayerCommand extends LayerCommand<DrawLayerOptions> {

    @Override
    String getName() {
        "vector draw"
    }

    @Override
    String getDescription() {
        "Draw a Layer to an Image, PDF, or SVG Document"
    }

    @Override
    DrawLayerOptions getOptions() {
        new DrawLayerOptions()
    }

    @Override
    protected void processLayer(Layer layer, DrawLayerOptions options, Reader reader, Writer writer) {
        geoscript.render.Map map = new geoscript.render.Map(
            layers: [layer],
            type: options.type,
            width: options.width,
            height: options.height
        )
        map.render(options.file ? options.file : new File("${['pdf','svg'].contains(options.type) ? "document" : "image"}.${options.type}"))
    }

    private static class DrawLayerOptions extends LayerOptions {

        @Option(name="-f", aliases="--file",  usage="The output file", required = false)
        File file

        @Option(name="-t", aliases="--type",  usage="The type of document", required = false)
        String type = "png"

        @Option(name="-w", aliases="--width",  usage="The width", required = false)
        int width = 800

        @Option(name="-h", aliases="--height",  usage="The height", required = false)
        int height = 600
    }

}
