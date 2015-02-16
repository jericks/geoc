package org.geocommands.geometry

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.workspace.Memory
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert a Geometry from one format to another.
 * @author Jared Erickson
 */
class ConvertCommand extends Command<ConvertOptions> {

    @Override
    String getName() {
        "geometry convert"
    }

    @Override
    String getDescription() {
        "Convert a geometry from one format to another"
    }

    @Override
    ConvertOptions getOptions() {
        new ConvertOptions()
    }

    @Override
    void execute(ConvertOptions options, Reader reader, Writer writer) throws Exception {
        Geometry geom = Geometry.fromString(options.input ? options.input : reader.text)
        if (options.type.equalsIgnoreCase("geometry")) {
            geoscript.geom.io.Writer geometryWriter = getGeometryWriter(options.format, options.formatOptions)
            writer.write(geometryWriter.write(geom))
        } else if (options.type.equalsIgnoreCase("feature")) {
            Schema schema = new Schema("feature", new Field("geom", geom.geometryType))
            Feature feature = schema.feature([geom: geom], "1")
            geoscript.feature.io.Writer featureWriter = getFeatureWriter(options.format, options.formatOptions)
            writer.write(featureWriter.write(feature))
        } else if (options.type.equalsIgnoreCase("layer")) {
            Schema schema = new Schema("feature", new Field("geom", geom.geometryType))
            Layer layer = new Memory().create(schema)
            Feature feature = schema.feature([geom: geom], "1")
            layer.add(feature)
            geoscript.layer.io.Writer layerWriter = getLayerWriter(options.format, options.formatOptions)
            writer.write(layerWriter.write(layer))
        } else {
            throw new IllegalArgumentException("Unknown output type '${options.type}'. Please use geometry, feature, or layer!")
        }
    }

    private geoscript.geom.io.Writer getGeometryWriter(String format, Map formatOptions) {
        if (format.equalsIgnoreCase("wkt")) {
            new geoscript.geom.io.WktWriter()
        } else if (format.equalsIgnoreCase("geojson")) {
            new geoscript.geom.io.GeoJSONWriter()
        } else if (format.equalsIgnoreCase("georss")) {
            new geoscript.geom.io.GeoRSSWriter()
        } else if (format.equalsIgnoreCase("gml2") || format.equalsIgnoreCase("gml")) {
            new geoscript.geom.io.Gml2Writer()
        } else if (format.equalsIgnoreCase("gml3")) {
            new geoscript.geom.io.Gml3Writer()
        } else if (format.equalsIgnoreCase("kml")) {
            new geoscript.geom.io.KmlWriter()
        } else if (format.equalsIgnoreCase("wkb")) {
            new geoscript.geom.io.WkbWriter()
        } else if (format.equalsIgnoreCase("gpx")) {
            new geoscript.geom.io.GpxWriter()
        } else {
            throw new IllegalArgumentException("Unknown format '${format}'!")
        }
    }

    private geoscript.feature.io.Writer getFeatureWriter(String format, Map formatOptions) {
        if (format.equalsIgnoreCase("geojson")) {
            new geoscript.feature.io.GeoJSONWriter()
        } else if (format.equalsIgnoreCase("gml")) {
            new geoscript.feature.io.GmlWriter()
        } else if (format.equalsIgnoreCase("kml")) {
            new geoscript.feature.io.KmlWriter()
        } else if (format.equalsIgnoreCase("georss")) {
            new geoscript.feature.io.GeoRSSWriter()
        } else if (format.equalsIgnoreCase("gpx")) {
            new geoscript.feature.io.GpxWriter()
        } else {
            throw new IllegalArgumentException("Unknown format '${format}'!")
        }
    }

    private geoscript.layer.io.Writer getLayerWriter(String format, Map formatOptions) {
        if (format.equalsIgnoreCase("csv")) {
            new geoscript.layer.io.CsvWriter()
        } else if (format.equalsIgnoreCase("geojson")) {
            new geoscript.layer.io.GeoJSONWriter()
        } else if (format.equalsIgnoreCase("gml")) {
            new geoscript.layer.io.GmlWriter()
        } else if (format.equalsIgnoreCase("kml")) {
            new geoscript.layer.io.KmlWriter()
        } else if (format.equalsIgnoreCase("georss")) {
            new geoscript.layer.io.GeoRSSWriter()
        } else if (format.equalsIgnoreCase("gpx")) {
            new geoscript.layer.io.GpxWriter()
        } else {
            throw new IllegalArgumentException("Unknown format '${format}'!")
        }
    }

    static class ConvertOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geometry", required = false)
        String input

        @Option(name = "-f", aliases = "--format", usage = "The output format (wkt, geojson, gml2, gml3, kml, georss, gpx, csv, wkb)", required = true)
        String format

        @Option(name = "-p", aliases = "--format-options", usage = "The output format options", required = false)
        Map<String, String> formatOptions

        @Option(name = "-t", aliases = "--type", usage = "The output type (geometry, feature, layer)", required = false)
        String type = "geometry"

    }
}
