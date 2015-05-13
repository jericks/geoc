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
        // Well known names
        if (str.equalsIgnoreCase("GlobalMercator")) {
            Pyramid.createGlobalMercatorPyramid()
        } else if (str.equalsIgnoreCase("GlobalMercatorBottomLeft")) {
            Pyramid.createGlobalMercatorPyramid(origin: Pyramid.Origin.BOTTOM_LEFT)
        }
        // JSON
        else if (str.startsWith("{")) {
            readJson(str)
        } else if (str.endsWith(".json")) {
            readJson(new File(str).text)
        }
        // XML
        else if (str.startsWith("<")) {
            readXml(str)
        } else if (str.endsWith(".xml")) {
            readXml(new File(str).text)
        }
        // Text
        else if (str.endsWith(".txt")) {
            readText(new File(str).text)
        } else {
            readText(str)
        }
    }

    /**
     * Write a Pyramid to a String.
     * @param pyramid The Pyramid
     * @param type The type of String (xml, json, text). Defaults to json.
     * @return A String
     */
    static String writePyramid(Pyramid pyramid, String type = "json") {
        if (type.equalsIgnoreCase("xml")) {
            writeXml(pyramid)
        } else if (type.equalsIgnoreCase("csv") || type.equalsIgnoreCase("text")) {
            writeText(pyramid)
        } else {
            writeJson(pyramid)
        }
    }

    /**
     * Write a Pyramid to a JSON String
     * @param pyramid The Pyramid
     * @return A JSON String
     */
    static String writeJson(Pyramid pyramid) {
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

    /**
     * Read a Pyramid from a JSON String
     * @param text The JSON String
     * @return A Pyramid
     */
    static Pyramid readJson(String text) {
        def slurper = new groovy.json.JsonSlurper()
        def json = slurper.parseText(text)
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

    /**
     * Write a Pyramid to an XML String
     * @param pyramid The Pyramid
     * @return An XML String
     */
    static String writeXml(Pyramid pyramid) {
        StringWriter writer = new StringWriter()
        def builder = new groovy.xml.MarkupBuilder(writer)
        builder.pyramid {
            proj pyramid.proj
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
            grids {
                pyramid.grids.each { Grid g ->
                    builder.grid {
                        z g.z
                        width g.width
                        height g.height
                        xres g.xResolution
                        yres g.yResolution
                    }
                }
            }
        }
        writer.toString()
    }

    /**
     * Read a Pyramid from an XML String
     * @param text The XML String
     * @return A Pyramid
     */
    static Pyramid readXml(String text) {
        def xml = new groovy.util.XmlParser().parseText(text)
        Projection projection = new Projection(xml.proj.text())
        Bounds bounds = new Bounds(xml.bounds.minX.text() as double, xml.bounds.minY.text() as double,
                xml.bounds.maxX.text() as double, xml.bounds.maxY.text() as double, projection)
        List<Grid> grids = xml.grids.grid.collect { def xmlGrid ->
            new Grid(xmlGrid.z.text() as long, xmlGrid.width.text() as long, xmlGrid.height.text() as long,
                    xmlGrid.xres.text() as double, xmlGrid.yres.text() as double)
        }
        Pyramid.Origin origin = Pyramid.Origin.valueOf(xml.origin.text())
        int tileWidth = xml.tileSize.width.text() as int
        int tileHeight = xml.tileSize.height.text() as int
        new Pyramid(proj: projection, bounds: bounds, grids: grids, origin: origin, tileWidth: tileWidth, tileHeight: tileHeight)
    }

    /**
     * Write a Pyramid to a CSV String
     * @param p The Pyramid
     * @return A CSV String
     */
    static String writeText(Pyramid p) {
        String NEW_LINE = System.getProperty("line.separator")
        StringBuilder builder = new StringBuilder()
        builder.append(p.proj.id).append(NEW_LINE)
        builder.append(p.bounds.minX).append(",").append(p.bounds.minY).append(",")
        builder.append(p.bounds.maxX).append(",").append(p.bounds.maxY).append(",")
        builder.append(p.bounds.proj ? p.bounds.proj.id : p.proj.id).append(NEW_LINE)
        builder.append(p.origin).append(NEW_LINE)
        builder.append(p.tileWidth).append(",").append(p.tileHeight).append(NEW_LINE)
        p.grids.each { Grid g ->
            builder.append(g.z).append(",")
            builder.append(g.width).append(",")
            builder.append(g.height).append(",")
            builder.append(g.xResolution).append(",")
            builder.append(g.yResolution).append(NEW_LINE)
        }
        builder.toString()
    }

    /**
     * Read a Pyramid from a CSV String
     * @param text The CSV String
     * @return A Pyramid
     */
    static Pyramid readText(String text) {
        Projection proj
        Bounds bounds
        List<Grid> grids = []
        Pyramid.Origin origin
        int tileWidth
        int tileHeight
        String NEW_LINE = System.getProperty("line.separator")
        text.split(NEW_LINE).eachWithIndex { String line, int i ->
            if (i == 0) {
                proj = new Projection(line)
            } else if (i == 1) {
                List parts = line.split(",")
                bounds = new Bounds(parts[0] as double, parts[1] as double, parts[2] as double, parts[3] as double,
                        parts.size() > 4 ? new Projection(parts[4]) : proj)
            } else if (i == 2) {
                origin = Pyramid.Origin.valueOf(line)
            } else if (i == 3) {
                List parts = line.split(",")
                tileWidth = parts[0] as int
                tileHeight = parts[1] as int
            } else if (i > 3) {
                List parts = line.split(",")
                Grid grid = new Grid(
                        parts[0] as long,
                        parts[1] as long,
                        parts[2] as long,
                        parts[3] as double,
                        parts[4] as double
                )
                grids.add(grid)
            }
        }
        new Pyramid(proj: proj, bounds: bounds, grids: grids, origin: origin, tileWidth: tileWidth, tileHeight: tileHeight)
    }

}
