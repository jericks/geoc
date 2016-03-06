package org.geocommands.map

import geoscript.geom.Bounds
import geoscript.layer.Format
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Renderable
import geoscript.layer.TileLayer
import geoscript.style.RasterSymbolizer
import geoscript.style.Style
import geoscript.style.Symbolizer
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDReader
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
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

        geoscript.render.Map map

        List<Renderable> layers = []
        if (options.layers) {
            map = new geoscript.render.Map()
            options.layers.each { Object layerObj ->
                LOGGER.info "Adding ${layerObj} to the Map!"
                Map params = [:]
                if (!(layerObj instanceof Map)) {
                    params.putAll(getParams(layerObj as String))
                } else {
                    params.putAll(layerObj as Map)
                }
                Renderable renderable = getRenderable(params)
                if (renderable) {
                    map.layers.add(renderable)
                } else {
                    LOGGER.warning "No Map Layer found for ${params}!"
                }
            }
        }

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

    private Renderable getRenderable(Map params) {
        LOGGER.info "getRenderable(${params})"
        Renderable renderable
        String layerType = params.get("layertype")
        String layerName = params.get("layername")
        String style = params.get("style")
        LOGGER.info "Layer Type = ${layerType} Layer Name = ${layerName} Style = ${style}"
        if (layerType.equalsIgnoreCase("layer")) {
            Workspace workspace = Workspace.getWorkspace(params)
            if (workspace) {
                LOGGER.info "Workspace = ${workspace.format}"
                Layer layer = workspace.get(layerName ?: workspace.names[0])
                if (params.layerprojection) {
                    layer.proj = params.layerprojection
                }
                if (style) {
                    layer.style = getStyle(layer, style)
                }
                renderable = layer
            } else if (params.containsKey("file")) {
                LOGGER.info "Reading layer from File = ${params['file']}"
                File file = new File(params.get("file"))
                if (file.exists()) {
                    // Try to use a Workspace first
                    try {
                        workspace = Workspace.getWorkspace(file.absolutePath)
                        if (workspace) {
                            LOGGER.info "Workspace = ${workspace.format}"
                            Layer layer = workspace.get(layerName ?: file.name)
                            if (params.layerprojection) {
                                layer.proj = params.layerprojection
                            }
                            if (style) {
                                layer.style = getStyle(layer, style)
                            }
                            renderable = layer
                        }
                    } catch(Exception ex) {
                        // Just try the Layer Readers
                    }
                    // Then try to use a Layer Reader
                    if (!renderable) {
                        geoscript.layer.io.Readers.list().each {
                            try {
                                Layer layer = it.read(file)
                                if (layer) {
                                    LOGGER.info "Reading Layer using ${it.class.simpleName}"
                                    if (style) {
                                        layer.style = getStyle(layer, style)
                                    }
                                    renderable = layer
                                    return
                                }
                            } catch (Exception e2) {
                            }
                        }
                    }
                }
            }
        } else if (layerType.equalsIgnoreCase("raster")) {
            Format format = Format.getFormat(params.get("source"))
            if (format) {
                LOGGER.info "Format = ${format}"
                Raster raster
                if (layerName) {
                    LOGGER.info "Reading Raster for ${layerName}"
                    raster = format.read(layerName)
                } else {
                    LOGGER.info "Reading Raster"
                    raster = format.read()
                }
                if (raster) {
                    if (style) {
                        raster.style = getStyle(raster, style)
                    }
                    renderable = raster
                } else {
                    LOGGER.warning "Unable to read Raster from Format!"
                }
            } else {
                LOGGER.warning "Unable to Find Raster Format for ${params}"
            }
        } else if (layerType.equalsIgnoreCase("tile")) {
            TileLayer tileLayer = TileLayer.getTileLayer(params)
            if (tileLayer) {
                LOGGER.info "TileLayer = ${tileLayer.name}"
                renderable = tileLayer
            } else if (params.containsKey("file")) {
                File file = new File(params.get("file"))
                LOGGER.info "Load TileLayer from File = ${file}"
                if (file.exists()) {
                    tileLayer = TileLayer.getTileLayer(file.absolutePath)
                    if (tileLayer) {
                        LOGGER.info "TileLayer = ${tileLayer.name}"
                        renderable = tileLayer
                    }
                }
            }
            else {
                LOGGER.warning "Unable to find TileLayer from ${params}"
            }
        } else {
            LOGGER.info "UNKNOWN layertype='${layerType}'!"
        }
        renderable
    }

    private Map getParams(String str) {
        Map params = [:]
        str.split("[ ]+(?=([^\']*\'[^\']*\')*[^\']*\$)").each {
            def parts = it.split("=")
            def key = parts[0].trim()
            if ((key.startsWith("'") && key.endsWith("'")) ||
                    (key.startsWith("\"") && key.endsWith("\""))) {
                key = key.substring(1, key.length() - 1)
            }
            def value = parts[1].trim()
            if ((value.startsWith("'") && value.endsWith("'")) ||
                    (value.startsWith("\"") && value.endsWith("\""))) {
                value = value.substring(1, value.length() - 1)
            }
            params.put(key, value)
        }
        params
    }

    private static Style getStyle(Layer layer, String styleStr) {
        Style style = Symbolizer.getDefault(layer.schema.geom.typ)
        getStyle(style, styleStr)
    }

    private static Style getStyle(Raster raster, String styleStr) {
        Style style = new RasterSymbolizer()
        getStyle(style, styleStr)
    }

    private static Style getStyle(Style defaultStyle, String styleStr) {
        Style style = defaultStyle
        File file = new File(styleStr)
        if (file.exists()) {
            if (file.name.endsWith(".sld")) {
                style = new SLDReader().read(file)
            } else {
                style = new CSSReader().read(file)
            }
        } else {
            try {
                style = new CSSReader().read(styleStr)
            } catch (Exception ex) {
                style = new SLDReader().read(styleStr)
            }
        }
        style
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
