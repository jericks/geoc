package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create a new Layer
 * @author Jared Erickson
 */
class CreateCommand extends LayerOutCommand<CreateOptions> {

    @Override
    String getName() {
        "vector create"
    }

    @Override
    String getDescription() {
        "Create a new Layer"
    }

    @Override
    CreateOptions getOptions() {
        new CreateOptions()
    }

    @Override
    Layer createLayer(CreateOptions options, Reader reader, Writer writer) throws Exception {
        // Get Workspace
        Workspace w
        if (!options.outputWorkspace) {
            w = new Memory()
        } else {
            w = Workspace.getWorkspace(options.outputWorkspace)
        }

        // Get Fields
        List<Field> fields = []
        options.fields.each { Map.Entry<String,String> f ->
            Field field
            if (f.value.contains("EPSG")) {
                def parts = f.value.split(" ")
                field = new Field(f.key, parts[0], parts[1].startsWith("EPSG") ? parts[1] : "EPSG:${parts[1]}")
            } else {
                field = new Field(f.key, f.value)
            }
            fields.add(field)
        }

        // Create Schema
        Schema schema = new Schema(getOutputLayerName(options, "layer"), fields)

        // Create Layer
        Layer layer = w.create(schema)
        layer
    }

    static class CreateOptions extends LayerOutOptions {

        @Option(name = "-f", aliases = "--field", usage = "A Field in the format 'name=type'", required = true)
        Map<String, String> fields

    }
}
