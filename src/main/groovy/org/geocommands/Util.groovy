package org.geocommands

import geoscript.layer.Format
import geoscript.layer.GeoTIFF
import geoscript.layer.Layer
import geoscript.layer.OSM
import geoscript.layer.Raster
import geoscript.layer.Renderable
import geoscript.layer.Shapefile
import geoscript.layer.TileLayer
import geoscript.style.RasterSymbolizer
import geoscript.style.Style
import geoscript.style.Symbolizer
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDReader
import geoscript.style.io.SimpleStyleReader
import geoscript.workspace.Workspace
import org.geotools.util.logging.Logging

import java.util.logging.Logger

/**
 * A Common set ot utilities
 * @author Jared Erickson
 */
class Util {

    /**
     * The Logger
     */
    private static final Logger LOGGER = Logging.getLogger("org.geocommands.Util")

    /**
     * Add one or more base map layers to a list of layers based on a base map string.
     * @param basemap The basemap string
     * @param layers A List of Layers
     */
    static void addBasemap(String basemap, List layers) {
        if (basemap.equalsIgnoreCase("osm")) {
            OSM osm = new OSM()
            layers.add(0, osm)
        } else if (basemap.endsWith(".shp")) {
            Shapefile shp = new Shapefile(basemap)
            layers.add(0, shp)
        } else if (basemap.endsWith(".properties")) {
            Layer layer = new geoscript.layer.Property(basemap)
            layers.add(0, layer)
        } else if (basemap.endsWith(".tif")) {
            GeoTIFF geotiff = new GeoTIFF(new File(basemap))
            Raster raster = geotiff.read()
            layers.add(0, raster)
        } else if (basemap.endsWith(".groovy")) {
            File file = new File(basemap)
            if (file.exists()) {
                String script = file.text
                Binding binding = new Binding()
                GroovyShell shell = new GroovyShell(binding)
                Object value = shell.evaluate(script)
                if (!(value instanceof List)) {
                    value = [value]
                }
                layers.addAll(0, value as List)
            }
        }

    }

    /**
     * Get a List of Renderable Map Layers from a List of Map Layer Strings
     * @param layers A List of Map Layer Strings
     * @return A List of Renderable Map Layers
     */
    static List<Renderable> getMapLayers(List<String> layers) {
        List<Renderable> renderables = []
        if (layers) {
            layers.each { Object layerObj ->
                LOGGER.info "Adding ${layerObj} to the Map!"
                Map params = [:]
                if (!(layerObj instanceof Map)) {
                    params.putAll(getParams(layerObj as String))
                } else {
                    params.putAll(layerObj as Map)
                }
                Renderable renderable = getRenderable(params)
                if (renderable) {
                    renderables.add(renderable)
                } else {
                    LOGGER.warning "No Map Layer found for ${params}!"
                }
            }
        }
        renderables
    }

    private static Renderable getRenderable(Map params) {
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
                LOGGER.info "Layer = ${layer}"
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
                            LOGGER.info "Layer = ${layer}"
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

    private static Map getParams(String str) {
        Map params = [:]
        str.split("[ ]+(?=([^\']*\'[^\']*\')*[^\']*\$)").each {
            def parts = it.split("[=]+(?=([^\']*\'[^\']*\')*[^\']*\$)")
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
        Style style = null
        File file = new File(styleStr)
        if (file.exists()) {
            if (file.name.endsWith(".sld")) {
                style = new SLDReader().read(file)
            } else if (file.name.endsWith(".css")){
                style = new CSSReader().read(file)
            } else if (file.name.endsWith(".txt")){
                style = new SimpleStyleReader().read(file)
            }
        }
        if (!style) {
            geoscript.style.io.Readers.list().each { geoscript.style.io.Reader reader ->
               try {
                   style = reader.read(styleStr)
                   return style
               }
               catch (Exception ex) {
               }
            }
        }
        if (!style) {
            style = defaultStyle
        }
        style
    }

    /**
     * Execute a Groovy Script
     * @param script The Groovy Script
     * @param args A Map of variables to provide the script
     * @return A value
     */
    static Object evaluateScript(String script, Map<String,Object> args) {
        Binding binding = new Binding()
        args.each{String key, Object value ->
            binding.setVariable(key, value)
        }
        GroovyShell shell = new GroovyShell(binding)
        shell.evaluate(script)
    }

}
