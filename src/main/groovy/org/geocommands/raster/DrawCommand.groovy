package org.geocommands.raster

import geoscript.layer.Raster
import geoscript.style.io.SLDReader
import org.kohsuke.args4j.Option

/**
 * Draw a Raster to an Image
 * @author Jared Erickson
 */
class DrawCommand extends RasterCommand<DrawOptions>{

    @Override
    String getName() {
        "raster draw"
    }

    @Override
    String getDescription() {
        "Draw a Raster to an image"
    }

    @Override
    DrawOptions getOptions() {
        new DrawOptions()
    }

    @Override
    protected void processRaster(Raster raster, DrawOptions options, Reader reader, Writer writer) throws Exception {
        if (options.sldFile) {
            def style = new SLDReader().read(options.sldFile)
            raster.style = style
        }
        geoscript.render.Map map = new geoscript.render.Map(
            layers: [raster],
            type: options.type,
            width: options.width,
            height: options.height
        )
        map.render(options.file ? options.file : new File("${['pdf','svg'].contains(options.type) ? "document" : "image"}.${options.type}"))
    }

    static class DrawOptions extends RasterOptions {
        @Option(name="-f", aliases="--file",  usage="The output file", required = false)
        File file

        @Option(name="-t", aliases="--type",  usage="The type of document", required = false)
        String type = "png"

        @Option(name="-w", aliases="--width",  usage="The width", required = false)
        int width = 800

        @Option(name="-h", aliases="--height",  usage="The height", required = false)
        int height = 600

        @Option(name="-s", aliases="--sld-file",  usage="The sld file", required = false)
        File sldFile
    }
}
