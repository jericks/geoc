package org.geocommands.map

import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.layer.Raster
import geoscript.proj.Projection
import geoscript.render.Map
import geoscript.render.MapCube
import org.geocommands.Options
import org.geocommands.Command
import org.geocommands.Util
import org.kohsuke.args4j.Option

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.util.List

/**
 * Create a Map Cube
 * @author Jared Erickson
 */
class MapCubeCommand extends Command<MapCubeOptions> {

    @Override
    String getName() {
        "map cube"
    }

    /**
     * Get the short description
     * @return A short description
     */
    @Override
    String getDescription() {
        "Create a map cube"
    }

    /**
     * Get the Options
     * @return The Options
     */
    @Override
    MapCubeOptions getOptions() {
        new MapCubeOptions()
    }

    /**
     * Execute the command
     * @param options The Options
     * @param reader The Reader
     * @param writer The Write
     * @throws Exception if an error occurs
     */
    @Override
    void execute(MapCubeOptions options, Reader reader, Writer writer) throws Exception {
        List layers = Util.getMapLayers(options.layers)
        MapCube mapCube = new MapCube(
                title: options.title,
                source: options.source,
                drawOutline: options.drawOutline,
                drawTabs: options.drawTabs,
                tabSize: options.tabSize
        )
        mapCube.render(layers, options.file)
    }

    static class MapCubeOptions extends Options {

        @Option(name = "-l", aliases = "--layer", usage = "The map layer", required = false)
        List<String> layers

        @Option(name = "-f", aliases = "--file", usage = "The output image file", required = false)
        File file = new File("mapcube.png")

        @Option(name = "-o", aliases = "--draw-outline", usage = "The flag to whether to draw outlines or not", required = false)
        boolean drawOutline = false

        @Option(name = "-t", aliases = "--draw-tabs", usage = "The flag to whether to draw tabs or not", required = false)
        boolean drawTabs = true

        @Option(name = "-s", aliases = "--tab-size", usage = "The tab size", required = false)
        int tabSize = 30

        @Option(name = "-i", aliases = "--title", usage = "The title", required = false)
        String title = ""

        @Option(name = "-c", aliases = "--source", usage = "The data source or credits", required = false)
        String source = ""
    }

}
