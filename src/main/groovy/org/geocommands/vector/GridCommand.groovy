package org.geocommands.vector

import geoscript.feature.Field
import geoscript.geom.Bounds
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create a vector Grid around an input Geometry.
 * @author Jared Erickson
 */
class GridCommand extends LayerOutCommand<GridOptions> {

    @Override
    String getName() {
        "vector grid"
    }

    @Override
    String getDescription() {
        "Create a vector grid"
    }

    @Override
    GridOptions getOptions() {
        new GridOptions()
    }

    @Override
    Layer createLayer(GridOptions options, Reader reader, Writer writer) throws Exception {
        // Create Layer
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(options.outputWorkspace)
        }
        Layer layer = workspace.create(getOutputLayerName(options, "grid"), [
                new Field("the_geom", options.type.equalsIgnoreCase("point") ? "POINT" : "POLYGON", options.projection),
                new Field("id", "int"),
                new Field("row", "int"),
                new Field("col", "int"),
                new Field("col_row", "String")
        ])

        // Generate the grid
        Bounds bounds = Geometry.fromString(options.geometry).bounds
        if (options.rows > -1 && options.columns > -1) {
            int id = 0
            layer.withWriter { geoscript.layer.Writer w ->
                bounds.generateGrid(options.columns, options.rows, options.type, { cell, col, row ->
                    w.add(layer.schema.feature([
                            "the_geom": cell,
                            "id"      : id,
                            "col"     : col,
                            "row"     : row,
                            "col_row" : "${col}_${row}"
                    ]))
                    id++
                })
            }
        } else if (options.cellWidth > -1 && options.cellHeight > -1) {
            int id = 0
            layer.withWriter { geoscript.layer.Writer w ->
                bounds.generateGrid(options.cellWidth, options.cellHeight, options.type, { cell, col, row ->
                    w.add(layer.schema.feature([
                            "the_geom": cell,
                            "id"      : id,
                            "col"     : col,
                            "row"     : row,
                            "col_row" : "${col}_${row}"
                    ]))
                    id++
                })
            }
        }

        layer
    }

    static class GridOptions extends LayerOutOptions {

        @Option(name = "-y", aliases = "--rows", usage = "The number of rows", required = false)
        int rows = -1

        @Option(name = "-x", aliases = "--columns", usage = "The number of columns", required = false)
        int columns = -1

        @Option(name = "-w", aliases = "--cell-width", usage = "The cell width", required = false)
        double cellWidth = -1

        @Option(name = "-h", aliases = "--cell-height", usage = "The cell height", required = false)
        double cellHeight = -1

        @Option(name = "-t", aliases = "--type", usage = "The grid cell type", required = false)
        String type = "polygon"

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = true)
        String geometry

        @Option(name = "-p", aliases = "--projection", usage = "The projection", required = false)
        String projection = "EPSG:4326"

    }
}
