package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.Raster
import geoscript.render.Window
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDReader
import org.geocommands.raster.DisplayRasterCommand.DisplayRasterOptions
import org.kohsuke.args4j.Option

/**
 * Display a Raster in a simple viewer
 * @author Jared Erickson
 */
class DisplayRasterCommand extends RasterCommand<DisplayRasterOptions> {

    @Override
    String getName() {
        "raster display"
    }

    @Override
    String getDescription() {
        "Display a Raster in a simple viewer"
    }

    @Override
    DisplayRasterOptions getOptions() {
        new DisplayRasterOptions()
    }

    @Override
    protected void processRaster(Raster raster, DisplayRasterOptions options, Reader reader, Writer writer) throws Exception {
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
                width: options.width,
                height: options.height,
                bounds: options.bounds ? Bounds.fromString(options.bounds) : raster.bounds,
                backgroundColor: options.backgroundColor
        )
        display(map, options)
    }

    protected void display(geoscript.render.Map map, DisplayRasterOptions options) {
        Window window = new Window(map)
    }

    static class DisplayRasterOptions extends RasterOptions {

        @Option(name = "-w", aliases = "--width", usage = "The width", required = false)
        int width = 800

        @Option(name = "-h", aliases = "--height", usage = "The height", required = false)
        int height = 600

        @Option(name = "-s", aliases = "--sld-file", usage = "The sld file", required = false)
        File sldFile

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-m", aliases = "--base-map", usage = "The base map (can be a OSM tile set like stamen-toner, stamen-toner-lite, stamen-watercolor, mapquest-street, mapquest-satellite, shapefile, or Groovy script that returns Layers)", required = false)
        String baseMap

        @Option(name = "-g", aliases = "--background-color", usage = "The background color", required = false)
        String backgroundColor

    }
}
