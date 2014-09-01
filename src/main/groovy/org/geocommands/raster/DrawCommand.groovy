package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.Raster
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDReader
import org.kohsuke.args4j.Option

/**
 * Draw a Raster to an Image
 * @author Jared Erickson
 */
class DrawCommand extends RasterCommand<DrawOptions> {

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
            if (options.sldFile.name.endsWith(".sld")) {
                raster.style = new SLDReader().read(options.sldFile)
            } else if (options.sldFile.name.endsWith(".css")) {
                raster.style = new CSSReader().read(options.sldFile)
            }
        }
        List layers = [raster]
        if (options.baseMap) {
            org.geocommands.Util.addBasemap(options.baseMap, layers)
        }
        geoscript.render.Map map = new geoscript.render.Map(
                layers: layers,
                type: options.type,
                width: options.width,
                height: options.height,
                bounds: options.bounds ? Bounds.fromString(options.bounds) : raster.bounds
        )
        map.render(options.file ? options.file : new File("${['pdf', 'svg'].contains(options.type) ? "document" : "image"}.${options.type}"))
    }

    static class DrawOptions extends RasterOptions {
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

        @Option(name = "-m", aliases = "--base-map", usage = "The base map (can be a OSM tile set, shapefile, or Groovy script that returns Layers)", required = false)
        String baseMap
    }
}
