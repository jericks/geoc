package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Format
import geoscript.layer.GeoPackage
import geoscript.layer.ImageTileLayer
import geoscript.layer.MBTiles
import geoscript.layer.Pyramid
import geoscript.layer.Raster
import geoscript.layer.TMS
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
        ImageTileLayer tileLayer
        if (options.tileLayer.endsWith(".mbtiles")) {
            tileLayer = new MBTiles(new File(options.tileLayer))
        } else if (options.tileLayer.endsWith(".gpkg")) {
            tileLayer = new GeoPackage(new File(options.tileLayer), options.tileLayerName)
        } else if (options.type in ["png","jpeg","jpg","gif"] && new File(options.tileLayer).isDirectory()) {
            Pyramid pyramid = PyramidUtil.readPyramid(options.pyramid)
            tileLayer = new TMS(options.tileLayerName, options.type, new File(options.tileLayer), pyramid)
        } else if (options.tileLayer.equalsIgnoreCase("osm")) {
            tileLayer = TileUtil.getOSMImageTileLayer(options.tileLayerName)
        }

        Raster raster
        if (options.bounds && !options.z) {
            raster = tileLayer.getRaster(Bounds.fromString(options.bounds), options.width, options.height)
        } else if (options.bounds && options.z) {
            raster = tileLayer.getRaster(tileLayer.tiles(Bounds.fromString(options.bounds), options.z))
        } else if (options.z && options.minX && options.minY && options.maxX && options.maxY) {
            raster = tileLayer.getRaster(tileLayer.tiles(options.z, options.minX, options.minY, options.maxX, options.maxY))
        } else if (options.z) {
            raster = tileLayer.getRaster(tileLayer.tiles(options.z))
        } else {
            throw new IllegalArgumentException("Wrong combination of options for stitching together a raster from a tile layer!")
        }

        RasterUtil.writeRaster(raster, options.outputFormat, options.outputRaster, writer)
    }

    static class StitchRasterOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-n", aliases = "--tile-layer-name", usage = "The tile layer name", required = false)
        String tileLayerName

        @Option(name = "-t", aliases = "--type", usage = "The type of tile layer(png, utfgrid, mvt, pbf)", required = false)
        String type = "png"

        @Option(name = "-p", aliases = "--pyramid", usage = "The pyramid", required = false)
        String pyramid = "GlobalMercator"

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

    static void main(String[] args) {
        File dir = new File("/Users/jericks/Desktop/countries")
        File file = new File("/Users/jericks/Desktop/countries.tif")
        StitchRasterOptions options = new StitchRasterOptions(
                tileLayer: dir.absolutePath,
                tileLayerName: "countries",
                z: 2,
                outputRaster: file.absolutePath
        )
        StitchRasterCommand cmd = new StitchRasterCommand()
        cmd.execute(options)
    }
}



