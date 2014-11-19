package org.geocommands

import geoscript.layer.OSM
import geoscript.layer.Shapefile

/**
 * A Common set ot utilities
 * @author Jared Erickson
 */
class Util {

    /**
     * Add one or more base map layers to a list of layers based on a base map string.
     * @param basemap The basemap string
     * @param layers A List of Layers
     */
    static void addBasemap(String basemap, List layers) {
        if (basemap.equalsIgnoreCase("osm")) {
            OSM osm = new OSM()
            layers.add(0, osm)
        } else if (basemap.equalsIgnoreCase("stamen-toner")) {
            OSM osm = new OSM("Stamen Toner", [
                    "http://a.tile.stamen.com/toner",
                    "http://b.tile.stamen.com/toner",
                    "http://c.tile.stamen.com/toner",
                    "http://d.tile.stamen.com/toner"
            ])
            layers.add(0, osm)
        } else if (basemap.equalsIgnoreCase("stamen-toner-lite")) {
            OSM osm = new OSM("Stamen Toner Lite", [
                    "http://a.tile.stamen.com/toner-lite",
                    "http://b.tile.stamen.com/toner-lite",
                    "http://c.tile.stamen.com/toner-lite",
                    "http://d.tile.stamen.com/toner-lite"
            ])
            layers.add(0, osm)
        } else if (basemap.equalsIgnoreCase("stamen-watercolor")) {
            OSM osm = new OSM("Stamen Watercolor", [
                    "http://a.tile.stamen.com/watercolor",
                    "http://b.tile.stamen.com/watercolor",
                    "http://c.tile.stamen.com/watercolor",
                    "http://d.tile.stamen.com/watercolor"
            ])
            layers.add(0, osm)
        } else if (basemap.equalsIgnoreCase("mapquest-street")) {
            OSM osm = new OSM("MapQuest Street Map", [
                    "http://otile1.mqcdn.com/tiles/1.0.0/map",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map"
            ])
            layers.add(0, osm)
        } else if (basemap.equalsIgnoreCase("mapquest-satellite")) {
            OSM osm = new OSM("MapQuest Satellite Map", [
                    "http://otile1.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile2.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile3.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile4.mqcdn.com/tiles/1.0.0/sat"
            ])
            layers.add(0, osm)
        } else if (basemap.endsWith(".shp")) {
            Shapefile shp = new Shapefile(basemap)
            layers.add(0, shp)
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
