package org.geocommands.tile

import geoscript.layer.GeoPackage
import geoscript.layer.MBTiles
import geoscript.layer.OSM
import geoscript.layer.Pyramid
import geoscript.layer.TMS
import geoscript.layer.TileLayer
import geoscript.layer.UTFGrid
import geoscript.layer.VectorTiles

/**
 * A set of Tile Utilities.
 * @author Jared Erickson
 */
class TileUtil {

    /**
     * Get a OSM TileLayer by name
     * @param name The name of the OSM TileLayer (stamen-toner, stamen-toner-lite, stamen-watercolor, mapquest-street,
     * mapquest-satellite, or osm)
     * @return
     */
    static OSM getOSMImageTileLayer(String name) {
        if (name.equalsIgnoreCase("stamen-toner")) {
            new OSM("Stamen Toner", [
                    "http://a.tile.stamen.com/toner",
                    "http://b.tile.stamen.com/toner",
                    "http://c.tile.stamen.com/toner",
                    "http://d.tile.stamen.com/toner"
            ])
        } else if (name.equalsIgnoreCase("stamen-toner-lite")) {
            new OSM("Stamen Toner Lite", [
                    "http://a.tile.stamen.com/toner-lite",
                    "http://b.tile.stamen.com/toner-lite",
                    "http://c.tile.stamen.com/toner-lite",
                    "http://d.tile.stamen.com/toner-lite"
            ])
        } else if (name.equalsIgnoreCase("stamen-watercolor")) {
            new OSM("Stamen Watercolor", [
                    "http://a.tile.stamen.com/watercolor",
                    "http://b.tile.stamen.com/watercolor",
                    "http://c.tile.stamen.com/watercolor",
                    "http://d.tile.stamen.com/watercolor"
            ])
        } else if (name.equalsIgnoreCase("mapquest-street")) {
            new OSM("MapQuest Street Map", [
                    "http://otile1.mqcdn.com/tiles/1.0.0/map",
                    "http://otile2.mqcdn.com/tiles/1.0.0/map",
                    "http://otile3.mqcdn.com/tiles/1.0.0/map",
                    "http://otile4.mqcdn.com/tiles/1.0.0/map"
            ])
        } else if (name.equalsIgnoreCase("mapquest-satellite")) {
            new OSM("MapQuest Satellite Map", [
                    "http://otile1.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile2.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile3.mqcdn.com/tiles/1.0.0/sat",
                    "http://otile4.mqcdn.com/tiles/1.0.0/sat"
            ])
        } else if (name.equalsIgnoreCase("osm")) {
            new OSM()
        }
    }

}
