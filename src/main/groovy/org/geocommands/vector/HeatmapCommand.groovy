package org.geocommands.vector

import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.process.Process
import geoscript.proj.Projection
import geoscript.workspace.Memory
import org.kohsuke.args4j.Option

/**
 * Create a heatmap of the input layer
 * @author Jared Erickson
 */
class HeatmapCommand extends VectorToRasterCommand<HeatmapOptions> {

    @Override
    String getName() {
        "vector heatmap"
    }

    @Override
    String getDescription() {
        "Create a heatmap of the input layer"
    }

    @Override
    HeatmapOptions getOptions() {
        new HeatmapOptions()
    }

    @Override
    Raster createRaster(Layer layer, HeatmapOptions options, Reader reader, Writer writer) {
        // Layer must have a projection
        if (!layer.proj) {
            layer = layer.reproject(new Projection("EPSG:4326"), new Memory(), "${layer.name}_projected")
        }
        // Bounds must have a projection
        Bounds bounds = Bounds.fromString(options.bounds)
        if (!bounds) {
            bounds = layer.bounds
        }
        if (!bounds.proj) {
            bounds.proj = "EPSG:4326"
        }
        Process process = new Process("vec:Heatmap")
        Map results = process.execute([
                data         : layer,
                radiusPixels : options.radiusPixels,
                pixelsPerCell: options.pixelsPerCell,
                outputWidth  : options.width,
                outputHeight : options.height,
                outputBBOX   : bounds,
                weightAttr   : options.weightAttr
        ])
        results.result as Raster
    }

    static class HeatmapOptions extends VectorToRasterOptions {

        @Option(name = "-r", aliases = "--radius-pixels", usage = "The radius of the density kernel in pixels", required = true)
        int radiusPixels

        @Option(name = "-a", aliases = "--weight-field", usage = "The name of the weight field", required = false)
        String weightAttr

        @Option(name = "-p", aliases = "--pixels-per-cell", usage = "The resolution of the computed grid", required = false)
        int pixelsPerCell = 1

        @Option(name = "-b", aliases = "--bounds", usage = "The output bounds", required = false)
        String bounds

        @Option(name = "-w", aliases = "--width", usage = "The output width", required = true)
        int width

        @Option(name = "-h", aliases = "--height", usage = "The output height", required = true)
        int height

    }
}
