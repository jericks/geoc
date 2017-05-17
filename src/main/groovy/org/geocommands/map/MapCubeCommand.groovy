package org.geocommands.map

import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.layer.Raster
import geoscript.proj.Projection
import geoscript.render.Map
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

        BufferedImage image = new BufferedImage(1800, 1500, BufferedImage.TYPE_INT_ARGB)
        Graphics2D g2d = image.createGraphics()
        g2d.paint = java.awt.Color.WHITE
        g2d.fillRect(0, 0, 1800, 1500)

        Raster preRaster = null
        if (layers.size() > 0) {
            File preFile = File.createTempFile("map", ".png")
            Map preMap = new Map(
                    width: 1600,
                    height: 800,
                    proj: new Projection("EPSG:4326"),
                    fixAspectRatio: false,
                    bounds: new Bounds(-180, -89.9, 180, 89.9, "EPSG:4326"),
                    layers: layers
            )
            preMap.render(preFile)

            preRaster = new Raster(ImageIO.read(preFile), new Bounds(-180, -90, 180, 90, "EPSG:4326"))
        } else {
            options.drawOutline = true
        }

        List cubes = [
                // north
                [id: "0", image: [500, 140], center: new Point(-45, 90 - 0.1), bounds: new Bounds(-180.0, 45.0, 180.0, 90.0, "EPSG:4326")],
                // equators
                [id: "1", image: [100, 540], center: new Point(-135, 0), bounds: new Bounds(-180 + 0.1, -35, -90 + 0.1, 35, "EPSG:4326")],
                [id: "2", image: [500, 540], center: new Point(-45, 0), bounds: new Bounds(-90 + 0.1, -35, 0, 35, "EPSG:4326")],
                [id: "3", image: [900, 540], center: new Point(45, 0), bounds: new Bounds(0, -35, 90 - 0.1, 35, "EPSG:4326")],
                [id: "4", image: [1300, 540], center: new Point(135, 0), bounds: new Bounds(90 - 0.1, -35, 180 - 0.1, 35, "EPSG:4326")],
                // south
                [id: "5", image: [500, 940], center: new Point(-45, -90 + 0.1), bounds: new Bounds(-180.0, -45.0, 180.0, -90.0, "EPSG:4326")]
        ]

        cubes.each { java.util.Map cube ->
            if (preRaster) {
                Point center = cube.center
                Projection p = new Projection("AUTO:97001,9001,${center.x},${center.y}")
                Bounds b = cube.bounds.reproject(p)
                Map map = new Map(
                        scaleComputation: "ogc",
                        layers: [preRaster],
                        width: 400,
                        height: 400,
                        fixAspectRatio: false,
                        backgroundColor: "white",
                        proj: p,
                        bounds: b
                )
                BufferedImage img = map.renderToImage()
                g2d.drawImage(img, cube.image[0], cube.image[1], null)
            }
            if (options.drawOutline) {
                g2d.paint = new Color(0, 0, 0)
                g2d.drawRect(cube.image[0], cube.image[1], 400, 400)
            }
        }

        if (options.drawTabs) {
            g2d.paint = new Color(0,0,0)
            // top
            drawTab(g2d, 100, 540, "top", options.tabSize)
            drawTab(g2d, 100 + 400, 540 - 400, "top", options.tabSize)
            drawTab(g2d, 100 + (400 * 2), 540, "top", options.tabSize)
            // bottom
            drawTab(g2d, 100, 940, "bottom", options.tabSize)
            drawTab(g2d, 100 + (400 * 2), 940, "bottom", options.tabSize)
            drawTab(g2d, 100 + (400 * 3), 940, "bottom", options.tabSize)
            // east
            drawTab(g2d, 100 + (400 * 4), 540, "east", options.tabSize)
        }

        g2d.dispose()
        ImageIO.write(image, "png", options.file)

    }

    protected void drawTab(Graphics2D g, int x, int y, String side, int tab) {
        int size = 400
        // top
        if (side.equalsIgnoreCase("top")) {
            g.drawLine(x, y, x+tab, y-tab)
            g.drawLine(x + tab, y - tab, x + size - tab, y - tab)
            g.drawLine(x + size, y, x + size - tab, y - tab)
        }
        // bottom
        else if (side.equalsIgnoreCase("bottom")) {
            g.drawLine(x, y, x+tab, y+tab)
            g.drawLine(x + tab, y + tab, x + size - tab, y + tab)
            g.drawLine(x + size, y, x + size - tab, y + tab)
        }
        // east
        else if (side.equalsIgnoreCase("east")) {
            g.drawLine(x, y, x + tab, y + tab)
            g.drawLine(x + tab, y + tab, x + tab, y + size - tab)
            g.drawLine(x, y + size, x + tab, y + size - tab)
        }
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

    }

}
