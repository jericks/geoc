package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import geoscript.layer.io.CsvWriter
import geoscript.layer.io.GeoJSONWriter
import geoscript.layer.io.GeoRSSWriter
import geoscript.layer.io.GmlWriter
import geoscript.layer.io.GpxWriter
import geoscript.layer.io.KmlWriter
import org.kohsuke.args4j.Option

/**
 * Write a Layer to a String format (CSV, GeoJSON, KML, GML)
 * @author Jared Erickson
 */
class ToCommand extends LayerCommand<ToOptions> {

    @Override
    String getName() {
        "vector to"
    }

    @Override
    String getDescription() {
        "Write a Layer to a String format (CSV, GeoJSON, KML, GML)"
    }

    @Override
    ToOptions getOptions() {
        new ToOptions()
    }

    @Override
    protected void processLayer(Layer layer, ToOptions options, Reader reader, Writer writer) throws Exception {
        geoscript.layer.io.Writer layerWriter
        if (options.format.equalsIgnoreCase("csv")) {
            CsvWriter.Type type = CsvWriter.Type.valueOf(options.formatOptions.get("type","WKT"))
            String xColumn = options.formatOptions.get("xColumn")
            String yColumn = options.formatOptions.get("yColumn")
            if (xColumn != null && yColumn != null) {
                layerWriter = new CsvWriter(options.formatOptions, xColumn, yColumn, type)
            } else {
                layerWriter = new CsvWriter(options.formatOptions, type)
            }
        } else if (options.format.equalsIgnoreCase("geojson")) {
            layerWriter = new GeoJSONWriter()
        } else if (options.format.equalsIgnoreCase("gml")) {
            layerWriter = new GmlWriter()
        } else if (options.format.equalsIgnoreCase("kml")) {

            layerWriter = new KmlWriter()
        } else if (options.format.equalsIgnoreCase("georss")) {
            ["feedTitle","feedDescription", "feedLink",
             "itemTitle", "itemId", "itemDescription", "itemDate", "itemGeometry"].each { String key ->
                replaceOptionWithClosure(options.formatOptions, key)
            }
            layerWriter = new GeoRSSWriter(options.formatOptions)
        } else if (options.format.equalsIgnoreCase("gpx")) {
            ["elevation","time","name","description","type"].each { String key ->
                replaceOptionWithClosure(options.formatOptions, key)
            }
            layerWriter = new GpxWriter(options.formatOptions)
        } else {
            throw new Exception("Unknown ${options.format} format!")
        }
        if (layerWriter instanceof GmlWriter) {
            double version = options.formatOptions.get("version", 2) as double
            boolean format = options.formatOptions.get("format", true) as boolean
            boolean bounds = options.formatOptions.get("bounds", false) as boolean
            boolean xmldecl = options.formatOptions.get("xmldecl", false) as boolean
            String nsprefix = options.formatOptions.get("nsprefix", "gsf") as String
            writer.write(layerWriter.write(layer, version, format, bounds, xmldecl, nsprefix).trim())
        } else {
            writer.write(layerWriter.write(layer).trim())
        }

    }

    private void replaceOptionWithClosure(Map options, String key) {
        if (options.containsKey(key)) {
            String script = options[key]
            options[key] = { Feature feature ->
                org.geocommands.Util.evaluateScript(script, [f: feature])
            }
        }
    }

    static class ToOptions extends LayerOptions {
        @Option(name = "-f", aliases = "--format", usage = "The string format (CSV, GeoJSON, KML, GML)", required = true)
        String format

        @Option(name = "-p", aliases = "--format-options", usage = "A format options 'key=value'", required = false)
        Map<String, String> formatOptions = [:]
    }

}
