package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.io.CsvWriter
import geoscript.layer.io.GeoJSONWriter
import geoscript.layer.io.GmlWriter
import geoscript.layer.io.KmlWriter
import org.geocommands.vector.LayerCommand
import org.geocommands.vector.LayerOptions
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
            layerWriter = new CsvWriter()
        } else if (options.format.equalsIgnoreCase("geojson")) {
            layerWriter = new GeoJSONWriter()
        } else if (options.format.equalsIgnoreCase("gml")) {
            layerWriter = new GmlWriter()
        } else if (options.format.equalsIgnoreCase("kml")) {
            layerWriter = new KmlWriter()
        } else {
            throw new Exception("Unknown ${options.format} format!")
        }
        writer.write(layerWriter.write(layer).trim())
    }

    static class ToOptions extends LayerOptions {
        @Option(name="-f", aliases="--format",  usage="The string format (CSV, GeoJSON, KML, GML)", required = true)
        String format
    }
}
