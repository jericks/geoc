package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import geoscript.layer.io.GeoJSONReader
import geoscript.layer.io.GmlReader
import geoscript.layer.io.KmlReader
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create a Layer from a string of KML, CSV, GML, or GeoJSON
 * @author Jared Erickson
 */
class FromCommand extends LayerOutCommand<FromOptions> {

    @Override
    String getName() {
        "vector from"
    }

    @Override
    String getDescription() {
        "Create a Layer from a string of KML, CSV, GML, or GeoJSON"
    }

    @Override
    FromOptions getOptions() {
        new FromOptions()
    }

    @Override
    Layer createLayer(FromOptions options, Reader reader, Writer writer) throws Exception {

        // Get Workspace
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = Workspace.getWorkspace(options.outputWorkspace)
        }

        // Read in the Layer
        geoscript.layer.io.Reader layerReader
        if (options.format.equalsIgnoreCase("csv")) {
            layerReader = new CsvReader()
        } else if (options.format.equalsIgnoreCase("geojson")) {
            layerReader = new GeoJSONReader()
        } else if (options.format.equalsIgnoreCase("gml")) {
            layerReader = new GmlReader()
        } else if (options.format.equalsIgnoreCase("kml")) {
            layerReader = new KmlReader()
        } else {
            throw new Exception("Unknown ${options.format} format!")
        }
        String text = options.text ?: reader.text
        Layer layer = layerReader.read(text)

        // Create output Schema
        List fields = layer.schema.fields
        // The KmlReader includes a lot of Fields we don't want
        if (options.format.equalsIgnoreCase("kml")) {
            fields = [
                    layer.schema.get("Geometry"),
                    layer.schema.get("name"),
                    layer.schema.get("description"),
            ]
        }
        Schema schema = new Schema(getOutputLayerName(options, "layer"), fields)

        // Change the Geometry Type if necessary (shapefiles can't have geometry Field types)
        if (options.geometryType != null && !schema.geom.typ.equalsIgnoreCase(options.geometryType)) {
            schema = schema.changeGeometryType(options.geometryType, schema.name)
        }

        // Create the output Layer
        Layer outLayer = workspace.create(schema)
        outLayer.withWriter { geoscript.layer.Writer w ->
            layer.eachFeature { f ->
                w.add(f)
            }
        }
        outLayer
    }

    static class FromOptions extends LayerOutOptions {

        @Option(name = "-t", aliases = "--text", usage = "The text", required = false)
        String text

        @Option(name = "-f", aliases = "--format", usage = "The string format (CSV, GeoJSON, KML, GML)", required = true)
        String format

        @Option(name = "-g", aliases = "--geometry-type", usage = "The geometry type", required = false)
        String geometryType

    }
}
