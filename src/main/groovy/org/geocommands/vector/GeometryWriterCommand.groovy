package org.geocommands.vector

import geoscript.layer.Layer

/**
 * Convert the input layer to a text stream of WKT geometries that can be read by the geom commands
 * @author Jared Erickson
 */
class GeometryWriterCommand extends LayerCommand<GeometryWriterOptions> {

    private final String NEW_LINE = System.getProperty("line.separator")

    @Override
    String getName() {
        "vector geomw"
    }

    @Override
    String getDescription() {
        "Convert the input layer to a text stream of WKT geometries that can be read by the geom commands"
    }

    @Override
    GeometryWriterOptions getOptions() {
        new GeometryWriterOptions()
    }

    @Override
    protected void processLayer(Layer layer, GeometryWriterOptions options, Reader reader, Writer writer) {
        boolean first = true
        layer.eachFeature {f ->
            if (!first) {
                writer.write(NEW_LINE)
            }
            writer.write(f.geom.wkt)
            first = false
        }
    }

    static class GeometryWriterOptions extends LayerOptions {

    }
}
