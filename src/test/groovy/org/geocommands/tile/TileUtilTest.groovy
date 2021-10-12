package org.geocommands.tile

import geoscript.layer.OSM
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The TileUtil Unit Test.
 * @author Jared Erickson
 */
class TileUtilTest {

    @Test void getOSMImageTileLayer() {
        OSM osm = TileUtil.getOSMImageTileLayer("stamen-toner")
        assertEquals("Stamen Toner", osm.name)
        osm = TileUtil.getOSMImageTileLayer("stamen-toner-lite")
        assertEquals("Stamen Toner Lite", osm.name)
        osm = TileUtil.getOSMImageTileLayer("stamen-watercolor")
        assertEquals("Stamen Watercolor", osm.name)
        osm = TileUtil.getOSMImageTileLayer("mapquest-street")
        assertEquals("MapQuest Street Map", osm.name)
        osm = TileUtil.getOSMImageTileLayer("mapquest-satellite")
        assertEquals("MapQuest Satellite Map", osm.name)
        osm = TileUtil.getOSMImageTileLayer("osm")
        assertEquals("OSM", osm.name)
    }

}
