package org.geocommands.vector

import geoscript.layer.Layer

/**
 * Get basic information about the Layer.
 * @author Jared Erickson
 */
class InfoCommand extends LayerCommand<InfoOptions> {

    @Override
    String getName() {
        "vector info"
    }

    @Override
    String getDescription() {
        "Get basic information about the Layer"
    }

    @Override
    InfoOptions getOptions() {
        new InfoOptions()
    }

    @Override
    protected void processLayer(Layer layer, InfoOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        writer.write("Name: ${layer.name}")
        writer.write(NEW_LINE)
        writer.write("Geometry: ${layer.schema.geom.typ}")
        writer.write(NEW_LINE)
        writer.write("Extent: ${layer.bounds.minX}, ${layer.bounds.minY}, ${layer.bounds.maxX}, ${layer.bounds.maxY}")
        writer.write(NEW_LINE)
        writer.write("Projection ID: ${layer.proj != null ? layer.proj.id : 'Unknown'}")
        writer.write(NEW_LINE)
        writer.write("Projection WKT: ${layer.proj != null ? layer.proj.wkt : 'Unknown'}")
        writer.write(NEW_LINE)
        writer.write("Feature Count: ${layer.count}")
        writer.write(NEW_LINE)
        writer.write("Fields:")
        layer.schema.fields.each {fld ->
            writer.write(NEW_LINE)
            writer.write("${fld.name}: ${fld.typ}")
        }
    }

    static class InfoOptions extends LayerOptions {
    }
}
