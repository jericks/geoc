package org.geocommands

import geoscript.layer.OSM
import geoscript.layer.Shapefile
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The Util Test
 * @author Jared Erickson
 */
class UtilTest extends BaseTest {

    @Test void evaluateScript() {
        int value = Util.evaluateScript("a + b", [a: 1, b: 5]) as int
        assertEquals(6, value)
    }

    @Test void addBasemap() {

        // OSM
        List layers = []
        Util.addBasemap("osm", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "OSM", (layers[0] as OSM).name

        // Stamen Toner
        layers = []
        Util.addBasemap("stamen-toner", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "Stamen Toner", (layers[0] as OSM).name

        // Stamen Toner Lite
        layers = []
        Util.addBasemap("stamen-toner-lite", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "Stamen Toner Lite", (layers[0] as OSM).name

        // Stamen Water Color
        layers = []
        Util.addBasemap("stamen-watercolor", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "Stamen Watercolor", (layers[0] as OSM).name

        // MapQuest Street
        layers = []
        Util.addBasemap("mapquest-street", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "MapQuest Street Map", (layers[0] as OSM).name

        // MapQuest Satellite
        layers = []
        Util.addBasemap("mapquest-satellite", layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof OSM
        assertEquals "MapQuest Satellite Map", (layers[0] as OSM).name

        // Shapefile
        File shpFile = getResource("crop_polys.shp")
        layers = []
        Util.addBasemap(shpFile.absolutePath, layers)
        assertEquals 1, layers.size()
        assertTrue layers[0] instanceof Shapefile

        // Groovy Script
        File groovyFile = new File(folder, "map.groovy")
        groovyFile.text = """
import geoscript.workspace.Memory
import geoscript.layer.*
import geoscript.geom.*
import geoscript.feature.*

Memory mem = new Memory()
Layer points = mem.create("points",[
    new Field("geom","Point","EPSG:4326"),
    new Field("id","int")
])

Layer polys = mem.create("polys",[
    new Field("geom","Polygon","EPSG:4326"),
    new Field("id","int")
])

points.withWriter { Writer writer ->
    Geometry.createRandomPoints(new Bounds(0,0,50,50).geometry, 10).geometries.eachWithIndex { Point pt, int i ->
        Feature f = writer.newFeature
        f.set([
            geom: pt,
            id: i
        ])
        writer.add(f)
    }
}

polys.withWriter { Writer writer ->
    Geometry.createRandomPoints(new Bounds(0,0,50,50).geometry, 10).geometries.eachWithIndex { Point pt, int i ->
        Feature f = writer.newFeature
        f.set([
            geom: pt.buffer(2),
            id: i
        ])
        writer.add(f)
    }
}

[
    points,
    polys
]
"""
        layers = []
        Util.addBasemap(groovyFile.absolutePath, layers)
        assertEquals 2, layers.size()
        assertEquals "points", layers[0].name
        assertEquals "polys", layers[1].name
    }

}
