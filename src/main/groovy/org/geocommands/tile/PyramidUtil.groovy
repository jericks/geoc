package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Grid
import geoscript.layer.Pyramid
import geoscript.proj.Projection

/**
 * A set of Pyramid utilities.
 * @author Jared Erickson
 */
class PyramidUtil {

    /**
     * Read a Pyramid from a String.  The String can be a commonly named pyramid (GlobalMercator, GlobalMercatorBottomLeft)
     * or a File with a Pyramid as JSON
     * @param str The string
     * @return A Pyramid
     */
    static Pyramid readPyramid(String str) {
        if (str.equalsIgnoreCase("GlobalMercator")) {
            Pyramid.createGlobalMercatorPyramid()
        } else if (str.equalsIgnoreCase("GlobalMercatorBottomLeft")) {
            Pyramid.createGlobalMercatorPyramid(origin: Pyramid.Origin.BOTTOM_LEFT)
        } else {
            def slurper = new groovy.json.JsonSlurper()
            def json = slurper.parseText(new File(str).text)
            Projection projection = new Projection(json.proj)
            Bounds bounds = new Bounds(json.bounds.minX, json.bounds.minY, json.bounds.maxX, json.bounds.maxY, projection)
            List<Grid> grids = json.grids.collect { def jsonGrid ->
                new Grid(jsonGrid.z, jsonGrid.width, jsonGrid.height, jsonGrid.xres, jsonGrid.yres)
            }
            Pyramid.Origin origin = Pyramid.Origin.valueOf(json.origin)
            int tileWidth = json.tileSize.width
            int tileHeight = json.tileSize.height
            new Pyramid(proj: projection, bounds: bounds, grids: grids, origin: origin, tileWidth: tileWidth, tileHeight: tileHeight)
        }
    }

    /**
     * Write a Pyramid to a JSON String.
     * @param pyramid The Pyramid
     * @return A JSON String
     */
    static String writePyramid(Pyramid pyramid) {
        def builder = new groovy.json.JsonBuilder()
        builder {
            proj "${pyramid.proj}"
            bounds {
                minX pyramid.bounds.minX
                minY pyramid.bounds.minY
                maxX pyramid.bounds.maxX
                maxY pyramid.bounds.maxY
            }
            origin pyramid.origin
            tileSize {
                width pyramid.tileWidth
                height pyramid.tileHeight
            }
            grids pyramid.grids.collect { Grid g ->
                builder {
                    z g.z
                    width g.width
                    height g.height
                    xres g.xResolution
                    yres g.yResolution
                }
            }
        }
        builder.toPrettyString()
    }

}
