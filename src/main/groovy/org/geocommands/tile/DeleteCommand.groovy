package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Tile
import geoscript.layer.TileCursor
import geoscript.layer.TileLayer
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Delete tiles from a tile layer
 * @author Jared Erickson
 */
class DeleteCommand extends Command<DeleteOptions> {

    @Override
    String getName() {
        "tile delete"
    }

    @Override
    String getDescription() {
        "Delete tiles from a tile layer"
    }

    @Override
    DeleteOptions getOptions() {
        new DeleteOptions()
    }

    @Override
    void execute(DeleteOptions options, Reader reader, Writer writer) throws Exception {
        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        if (options.tile) {
            List parts = options.tile.split("/")
            long z = parts[0] as long
            long x = parts[1] as long
            long y = parts[2] as long
            Tile tile = tileLayer.get(z,x,y)
            tileLayer.delete(tile)
        } else {
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
                throw new IllegalArgumentException("Wrong combination of options for delete tiles!")
            }
            tileLayer.delete(tileCursor)
        }
    }

    static class DeleteOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-i", aliases = "--tile", usage = "The Tile Z/X/Y coordinates", required = false)
        String tile

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
