package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.GeoPackage
import geoscript.layer.ImageTileLayer
import geoscript.layer.MBTiles
import geoscript.layer.Pyramid
import geoscript.layer.Raster
import geoscript.layer.TMS
import geoscript.layer.TileLayer
import geoscript.layer.VectorTiles
import org.geocommands.Command
import org.geocommands.Options
import org.geocommands.raster.RasterUtil
import org.kohsuke.args4j.Option

/**
 * Stitch image tiles together to create a Raster.
 * @author Jared Erickson
 */
class StitchRasterCommand extends Command<StitchRasterOptions> {

    @Override
    String getName() {
        "tile stitch raster"
    }

    @Override
    String getDescription() {
        "Stitch image tiles together to create a Raster"
    }

    @Override
    StitchRasterOptions getOptions() {
        new StitchRasterOptions()
    }

    @Override
    void execute(StitchRasterOptions options, Reader reader, Writer writer) throws Exception {
        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        if (!tileLayer instanceof ImageTileLayer) {
            throw new IllegalArgumentException("Tile Layer must be an Image Tile Layer!")
            return
        }
        ImageTileLayer imageTileLayer = tileLayer as ImageTileLayer

        Raster raster
        if (options.bounds && !options.z) {
            raster = imageTileLayer.getRaster(Bounds.fromString(options.bounds), options.width, options.height)
        } else if (options.bounds && options.z) {
            raster = imageTileLayer.getRaster(imageTileLayer.tiles(Bounds.fromString(options.bounds), options.z))
        } else if (options.z && options.minX && options.minY && options.maxX && options.maxY) {
            raster = imageTileLayer.getRaster(imageTileLayer.tiles(options.z, options.minX, options.minY, options.maxX, options.maxY))
        } else if (options.z) {
            raster = imageTileLayer.getRaster(imageTileLayer.tiles(options.z))
        } else {
            throw new IllegalArgumentException("Wrong combination of options for stitching together a raster from a tile layer!")
        }

        RasterUtil.writeRaster(raster, options.outputFormat, options.outputRaster, writer)
    }

    static class StitchRasterOptions extends Options {

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

        @Option(name = "-o", aliases = "--output-raster", usage = "The output raster", required = false)
        String outputRaster

        @Option(name = "-f", aliases = "--output-raster-format", usage = "The output raster format", required = false)
        String outputFormat

    }
}



