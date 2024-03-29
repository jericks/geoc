package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDReader
import org.kohsuke.args4j.Option

/**
 * Draw a Layer to an Image, PDF, SVG Document
 * @author Jared Erickson
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
        if (options.sldFile) {
            if (options.sldFile.name.endsWith(".sld")) {
                layer.style = new SLDReader().read(options.sldFile)
            } else if (options.sldFile.name.endsWith(".css")) {
                layer.style = new CSSReader().read(options.sldFile)
            }
        }
        List layers = [layer]
        if (options.layers) {
            layers.addAll(org.geocommands.Util.getMapLayers(options.layers))
        }
        geoscript.render.Map map = new geoscript.render.Map(
                layers: layers,
                type: options.type,
                width: options.width,
                height: options.height,
                bounds: options.bounds ? Bounds.fromString(options.bounds) : layer.bounds,
                backgroundColor: options.backgroundColor
        )
        map.render(options.file ? options.file : new File("${['pdf', 'svg'].contains(options.type) ? "document" : "image"}.${options.type}"))
    }

    static class DrawLayerOptions extends LayerOptions {

        @Option(name = "-f", aliases = "--file", usage = "The output file", required = false)
        File file

        @Option(name = "-t", aliases = "--type", usage = "The type of document", required = false)
        String type = "png"

        @Option(name = "-w", aliases = "--width", usage = "The width", required = false)
        int width = 800

        @Option(name = "-h", aliases = "--height", usage = "The height", required = false)
        int height = 600

        @Option(name = "-s", aliases = "--sld-file", usage = "The sld file", required = false)
        File sldFile

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-m", aliases = "--layer", usage = "The map layer", required = false)
        List<String> layers

        @Option(name = "-g", aliases = "--background-color", usage = "The background color", required = false)
        String backgroundColor
    }

}
