package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Convert a vector Layer to a Raster
 * @author Jared Erickson
 */
class RasterCommand extends VectorToRasterCommand<RasterOptions> {

    @Override
    String getName() {
        "vector raster"
    }

    @Override
    String getDescription() {
        "Convert a vector Layer to a Raster"
    }

    @Override
    RasterOptions getOptions() {
        new RasterOptions()
    }

    @Override
    Raster createRaster(Layer layer, RasterOptions options, Reader reader, Writer writer) {
        List gridSize = options.gridSize.split(",").collect{it as int}
        Bounds bounds = Bounds.fromString(options.bounds)
        if (!bounds) {
             bounds = layer.bounds
        }
        String rasterName = options.rasterName ?: "${layer.name}_raster"
        layer.getRaster(options.field, gridSize, bounds, rasterName)
    }

    static class RasterOptions extends VectorToRasterOptions {

        @Option(name="-d", aliases="--field",  usage="The field name with value", required = true)
        String field

        @Option(name="-s", aliases="--grid-size",  usage="The grid size", required = true)
        String gridSize

        @Option(name="-b", aliases="--bounds",  usage="The bounds", required = false)
        String bounds

        @Option(name="-n", aliases="--raster-name",  usage="The raster name", required = false)
        String rasterName
    }
}
