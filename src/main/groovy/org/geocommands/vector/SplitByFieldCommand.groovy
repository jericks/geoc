package org.geocommands.vector

import geoscript.feature.Field
import geoscript.filter.Filter
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Split a Layer in separate Layers based on values from a Field.
 * @author Jared Erickson
 */
class SplitByFieldCommand extends LayerCommand<SplitByFieldOptions> {

    @Override
    String getName() {
        "vector splitbyfield"
    }

    @Override
    String getDescription() {
        "Split a Layer into separate Layers based on values from a Field"
    }

    @Override
    SplitByFieldOptions getOptions() {
        new SplitByFieldOptions()
    }

    @Override
    protected void processLayer(Layer layer, SplitByFieldOptions options, Reader reader, Writer writer) throws Exception {

        Field field = layer.schema.get(options.field)

        Workspace workspace = options.outputWorkspace ? new Workspace(options.outputWorkspace) : new Memory()

        // Get unique values
        Set values = []
        layer.eachFeature { f ->
            values.add(f.get(field))
        }

        String NEW_LINE = System.getProperty("line.separator")

        // The quote character for creating a CQL Filter
        String quote = field.typ.equalsIgnoreCase("String") ? "'" : ""

        // For each unique value create a Layer and add Features
        values.eachWithIndex { v, i ->
            Layer outLayer = workspace.create("${layer.name}_${field.name}_${v.toString().replaceAll(' ', '_')}", layer.schema.fields)
            Filter filter = new Filter("${field.name} = ${quote}${v}${quote}")
            outLayer.withWriter { geoscript.layer.Writer w ->
                layer.getFeatures(filter).each { f ->
                    w.add(f)
                }
            }
            if (workspace instanceof Memory) {
                if (i > 0) writer.write(NEW_LINE)
                writer.write(outLayer.name)
                writer.write(NEW_LINE)
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
        }
    }

    static class SplitByFieldOptions extends LayerOptions {

        @Option(name = "-f", aliases = "--field", usage = "The field name", required = true)
        String field

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace

    }
}
