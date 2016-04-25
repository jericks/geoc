package org.geocommands.map

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Renderable
import geoscript.layer.TileLayer
import org.geocommands.Command
import org.geocommands.Options
import org.geocommands.Util
import org.geotools.util.logging.Logging
import org.kohsuke.args4j.Option

import java.util.logging.Logger

/**
 * A Command to draw a Map.
 * @author Jared Erickson
 */
class MapCommand extends Command<MapOptions>{

    /**
     * The Logger
     */
    private static final Logger LOGGER = Logging.getLogger("org.geocommands.map.MapCommand");
    
    /**
     * Get the name of the command
     * @return The name of the command
     */
    @Override
    String getName() {
        "map draw"
    }

    /**
     * Get the short description
     * @return A short description
     */
    @Override
    String getDescription() {
        "Draw a Map"
    }

    /**
     * Get the Options
     * @return The Options
     */
    @Override
    MapOptions getOptions() {
        new MapOptions()
    }

    /**
     * Execute the command
     * @param options The Options
     * @param reader The Reader
     * @param writer The Write
     * @throws Exception if an error occurs
     */
    @Override
    void execute(MapOptions options, Reader reader, Writer writer) throws Exception {

        LOGGER.info "Drawing Map with ${options}"

        geoscript.render.Map map = new geoscript.render.Map()

        List<Renderable> layers = Util.getMapLayers(options.layers)
        map.layers = layers

        map.type = options.type
        map.width = options.width
        map.height = options.height
        if (options.projection) {
            map.proj = options.projection
        }
        if (options.bounds) {
            map.bounds = Bounds.fromString(options.bounds)
        }
        map.backgroundColor = options.backgroundColor

        File file = options.file ? options.file : new File("${['pdf', 'svg'].contains(options.type) ? "document" : "image"}.${options.type}")
        try {
            map.render(file)
        } finally {
            map.layers.each { Renderable renderable ->
                if (renderable instanceof Layer) {
                    (renderable as Layer).workspace.close()
                } else if (renderable instanceof TileLayer) {
                    (renderable as TileLayer).close()
                } else if (renderable instanceof Raster) {
                    (renderable as Raster).dispose()
                }
            }
            map.close()
        }
    }

    static class MapOptions extends Options {

        /**
         * Map Layer Strings are space delimited with key=value pairs
         * layertype=layer|raster|tile
         * layername=The name of the layer
         * style=A Path to a SLD or CSS File
         * For layer layertype, you can use the same key value pairs used to specify a Workspace.
         * For raster layertype, you specify a source=raster key value pair
         * For tile layertype, you use the same key value pairs used to specify a tile layer.
         *
         * Examples:
         *
         * layertype=layer dbtype=geopkg database=/Users/user/Desktop/countries.gpkg layername=countries style=/Users/user/Desktop/countries.sld"
         *
         * layertype=layer file=/Users/user/Desktop/geoc/polygons.csv layername=polygons style=/Users/user/Desktop/geoc/polygons.sld"
         *
         * layertype=layer file=/Users/user/Desktop/geoc/points.properties style=/Users/user/Desktop/geoc/points.sld
         *
         * layertype=layer file=/Users/user/Projects/geoc/src/test/resources/polygons.shp"
         *
         * layertype=layer directory=/Users/user/Projects/geoc/src/test/resources/points.properties layername=points"
         *
         * layertype=raster source=rasters/earth.tif
         *
         * layertype=tile file=world.mbtiles
         *
         * layertype=tile type=geopackage file=states.gpkg
         */
        @Option(name = "-l", aliases = "--layer", usage = "The map layer", required = true)
        List<String> layers

        @Option(name = "-i", aliases = "--layer-file", usage = "The input layer file", required = false)
        File inputFile

        @Option(name = "-f", aliases = "--file", usage = "The output image file", required = false)
        File file

        @Option(name = "-t", aliases = "--type", usage = "The type of document", required = false)
        String type = "png"

        @Option(name = "-w", aliases = "--width", usage = "The width", required = false)
        int width = 800

        @Option(name = "-h", aliases = "--height", usage = "The height", required = false)
        int height = 600

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-g", aliases = "--background-color", usage = "The background color", required = false)
        String backgroundColor

        @Option(name = "-p", aliases = "--projection", usage = "The projection", required = false)
        String projection
    }

}
