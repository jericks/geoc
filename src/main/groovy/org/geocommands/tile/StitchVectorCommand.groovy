package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Pyramid
import geoscript.layer.TileCursor
import geoscript.layer.TileLayer
import geoscript.layer.VectorTiles
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Stitch vector tiles together to create a one or more Layers.
 * @author Jared Erickson
 */
class StitchVectorCommand extends Command<StitchVectorOptions> {

    @Override
    String getName() {
        "tile stitch vector"
    }

    @Override
    String getDescription() {
        "Stitch vector tiles together to create a one or more Layers"
    }

    @Override
    StitchVectorOptions getOptions() {
        new StitchVectorOptions()
    }

    @Override
    void execute(StitchVectorOptions options, Reader reader, Writer writer) throws Exception {
        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        if (tileLayer == null) {
            throw new IllegalArgumentException("Can't get tile layer from ${options.tileLayer}!")
        }
        if (!tileLayer instanceof VectorTiles) {
            throw new IllegalArgumentException("Tile Layer must be a Vector Tile Layer!")
        }
        VectorTiles vectorTiles = tileLayer as VectorTiles
        try {
            TileCursor tileCursor
            if (options.bounds && !options.z) {
                tileCursor = vectorTiles.tiles(Bounds.fromString(options.bounds), options.width, options.height)
            } else if (options.bounds && options.z) {
                tileCursor = vectorTiles.tiles(Bounds.fromString(options.bounds), options.z)
            } else if (options.z && options.minX && options.minY && options.maxX && options.maxY) {
                tileCursor = vectorTiles.tiles(options.z, options.minX, options.minY, options.maxX, options.maxY)
            } else if (options.z) {
                tileCursor = vectorTiles.tiles(options.z)
            } else {
                throw new IllegalArgumentException("Wrong combination of options for stitching together layers from vectortiles!")
            }
            List layers = vectorTiles.getLayers(tileCursor)

            Workspace workspace
            if (!options.outputWorkspace) {
                workspace = new Memory()
            } else {
                workspace = Workspace.getWorkspace(options.outputWorkspace)
            }

            try {
                layers.each { Layer layer ->
                    workspace.add(layer)
                }
            } finally {
                workspace.close()
            }

        } finally {
            vectorTiles.close()
        }

    }

    static class StitchVectorOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-w", aliases = "--width", usage = "The raster width", required = false)
        int width = 400

        @Option(name = "-h", aliases = "--height", usage = "The raster height", required = false)
        int height = 400

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

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace
    }

}
