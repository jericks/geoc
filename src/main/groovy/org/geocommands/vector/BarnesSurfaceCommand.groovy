package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.process.Process
import geoscript.proj.Projection
import geoscript.workspace.Memory
import org.kohsuke.args4j.Option

/**
 * Create a barnes surface of the features from the input layer
 * @author Jared Erickson
 */
class BarnesSurfaceCommand extends VectorToRasterCommand<BarnesSurfaceOptions> {

    @Override
    String getName() {
        "vector barnessurface"
    }

    @Override
    String getDescription() {
        "Create a barnes surface of the features from the input layer"
    }

    @Override
    BarnesSurfaceOptions getOptions() {
        new BarnesSurfaceOptions()
    }

    @Override
    Raster createRaster(Layer layer, BarnesSurfaceOptions options, Reader reader, Writer writer) {

        // Bounds must have a projection
        Bounds bounds = Bounds.fromString(options.bounds)
        if (!bounds) {
            bounds = layer.bounds
        }
        if (!bounds.proj) {
            bounds.proj = "EPSG:4326"
        }

        // Layer must have a projection
        if (!layer.proj) {
            layer = layer.reproject(new Projection("EPSG:4326"), new Memory(), "${layer.name}_projected")
        }

        // The valueAttr Field must be numeric, if it comes from CSV then it's probably a string
        // so convert it to double
        Field fld = layer.schema.get(options.valueAttr)
        if (fld.typ.equalsIgnoreCase("string")) {
            Schema schema = layer.schema.changeField(fld, new Field(options.valueAttr, "double"), "${layer.name}_numeric_${options.valueAttr}")
            Layer newLayer = new Memory().create(schema)
            newLayer.add(layer.cursor)
            layer = newLayer
        }

        Process process = new Process("vec:BarnesSurface")
        Map results = process.execute([
                data                  : layer,
                valueAttr             : options.valueAttr,
                dataLimit             : options.dataLimit,
                scale                 : options.scale,
                convergence           : options.convergence,
                passes                : options.passes,
                minObservations       : options.minObservations,
                maxObservationDistance: options.maxObservationDistance,
                noDataValue           : options.noDataValue,
                pixelsPerCell         : options.pixelsPerCell,
                queryBuffer           : options.queryBuffer,
                outputWidth           : options.width,
                outputHeight          : options.height,
                outputBBOX            : Bounds.fromString(options.bounds) ?: layer.bounds,
        ])
        results.result as Raster
    }

    static class BarnesSurfaceOptions extends VectorToRasterOptions {

        @Option(name = "-v", aliases = "--value-field", usage = "The name of the value field", required = true)
        String valueAttr

        @Option(name = "-d", aliases = "--data-limit", usage = "The maximum number of features to process", required = false)
        int dataLimit

        @Option(name = "-s", aliases = "--scale", usage = "The interpolation length", required = true)
        double scale

        @Option(name = "-c", aliases = "--convergence", usage = "The refinement factor", required = false)
        double convergence = 0.3

        @Option(name = "-p", aliases = "--passes", usage = "The number of passes", required = false)
        int passes = 1

        @Option(name = "-m", aliases = "--min-observations", usage = "The minimum number of observations to be a grid cell", required = false)
        int minObservations = 0

        @Option(name = "-x", aliases = "--max-observation-distance", usage = "The max distance for an observation to be a grid cell", required = false)
        int maxObservationDistance = 0

        @Option(name = "-n", aliases = "--no-data", usage = "The no data value", required = false)
        String noDataValue

        @Option(name = "-e", aliases = "--pixels-per-cell", usage = "The resolution of the computed grid", required = false)
        int pixelsPerCell = 1

        @Option(name = "-q", aliases = "--query-buffer", usage = "The query buffer", required = false)
        double queryBuffer = 0

        @Option(name = "-b", aliases = "--bounds", usage = "The output bounds", required = false)
        String bounds

        @Option(name = "-w", aliases = "--width", usage = "The output width", required = true)
        int width

        @Option(name = "-h", aliases = "--height", usage = "The output height", required = true)
        int height
    }
}
