package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.TileCursor
import geoscript.layer.TileLayer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.vector.LayerOutCommand
import org.geocommands.vector.LayerOutOptions
import org.kohsuke.args4j.Option

/**
 * Create a vector grid of a tile layers cells.
 * @author Jared Erickson
 */
class VectorGridCommand extends LayerOutCommand<VectorGridOptions> {

    @Override
    String getName() {
        "tile vector grid"
    }

    @Override
    String getDescription() {
        "Create a vector grid of a tile layers cells."
    }

    @Override
    VectorGridOptions getOptions() {
        new VectorGridOptions()
    }

    @Override
    Layer createLayer(VectorGridOptions options, Reader reader, Writer writer) throws Exception {
        Layer layer
        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        try {
            TileCursor tileCursor
            if (options.bounds && !options.z) {
                tileCursor = tileLayer.tiles(Bounds.fromString(options.bounds), options.width, options.height)
            } else if (options.bounds && options.z) {
                tileCursor = tileLayer.tiles(Bounds.fromString(options.bounds), options.z)
            } else if (options.z && options.minX && options.minY && options.maxX && options.maxY) {
                tileCursor = tileLayer.tiles(options.z, options.minX, options.minY, options.maxX, options.maxY)
            } else if (options.z) {
                tileCursor = tileLayer.tiles(options.z)
            } else {
                throw new IllegalArgumentException("Wrong combination of options!")
            }

            Workspace workspace
            if (!options.outputWorkspace) {
                workspace = new Memory()
            } else {
                workspace = Workspace.getWorkspace(options.outputWorkspace)
            }

            layer = tileLayer.getLayer(tileCursor,
                    outWorkspace: workspace,
                    outLayer: getOutputLayerName(options, "${tileLayer.name}_tiles")
            )
        } finally {
            tileLayer.close()
        }
        layer
    }

    static class VectorGridOptions extends LayerOutOptions {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-z", aliases = "--zoom-level", usage = "The tile zoom level", required = false)
        Long z = null

        @Option(name = "-x", aliases = "--minx", usage = "The min x or col", required = false)
        Long minX = null

        @Option(name = "-y", aliases = "--miny", usage = "The min y or row", required = false)
        Long minY = null

        @Option(name = "-c", aliases = "--maxx", usage = "The max x or col", required = false)
        Long maxX = null

        @Option(name = "-u", aliases = "--maxy", usage = "The max y or row", required = false)
        Long maxY = null

        @Option(name = "-w", aliases = "--width", usage = "The raster width", required = false)
        int width = 400

        @Option(name = "-h", aliases = "--height", usage = "The raster height", required = false)
        int height = 400

    }
}
