package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.layer.Layer
import geoscript.workspace.Database
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get a Layer from a Database using a SELECT statement.
 * @author Jared Erickson
 */
class DatabaseSelectCommand extends Command<DatabaseSelectOptions> {

    @Override
    String getName() {
        "vector database select"
    }

    @Override
    String getDescription() {
        "Get a Layer from a Database using a SELECT statement"
    }

    @Override
    DatabaseSelectOptions getOptions() {
        new DatabaseSelectOptions()
    }

    @Override
    void execute(DatabaseSelectOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            String[] fieldParts = options.geometryField.split("\\|")
            Field geometryField = new Field(
                    fieldParts.length > 0 ? fieldParts[0] : "the_geom",
                    fieldParts.length > 1 ? fieldParts[1] : "Geometry",
                    fieldParts.length > 2 ? fieldParts[2] : "EPSG:4326"
            )
            Layer layer = database.createView(options.layerName, options.sql, geometryField, primaryKeyFields: options.primaryKeyFields)

            Layer outLayer = Util.getOutputLayer(layer, options.outputWorkspace, options.outputLayer)
            try {
                outLayer.withWriter {geoscript.layer.Writer w ->
                    layer.eachFeature {Feature f ->
                        w.add(f)
                    }
                }
                if (outLayer.workspace instanceof Memory) {
                    writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
                }
            }
            finally {
                outLayer.workspace.close()
            }
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseSelectOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-l", aliases = "--layer-name", usage = "The input layer", required = true)
        String layerName

        @Option(name = "-s", aliases = "--sql", usage = "The input layer", required = true)
        String sql

        @Option(name = "-g", aliases = "--geometry-field", usage = "The geometry field (name|type|projection)", required = true)
        String geometryField

        @Option(name = "-p", aliases = "--primary-key-field", usage = "The primary key field names", required = false)
        List<String> primaryKeyFields

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace

        @Option(name = "-r", aliases = "--output-layer", usage = "The output layer", required = false)
        String outputLayer

    }

}
