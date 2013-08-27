package org.geocommands.raster

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.vector.Util
import org.kohsuke.args4j.Option

/**
 * Create contours from a Raster
 * @author Jared Erickson
 */
class ContourCommand extends RasterToVectorCommand<ContourOptions> {

    @Override
    String getName() {
        "raster contour"
    }

    @Override
    String getDescription() {
        "Create contours from a Raster"
    }

    @Override
    ContourOptions getOptions() {
        new ContourOptions()
    }

    @Override
    void convertRasterToVector(Raster raster, Layer layer, ContourOptions options, Reader reader, Writer writer) throws Exception {
        Layer contourLayer = raster.contours(options.band, options.levels.size() == 1 ? options.levels[0] : options.levels,
            options.simplify, options.smooth, Bounds.fromString(options.bounds))
        layer.add(contourLayer.cursor)
    }

    @Override
    protected Schema createOutputSchema(Raster raster, ContourOptions options) {
        new Schema(Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, name.replaceAll(" ","_")), [
            new Field("the_geom", "LineString"),
            new Field("value", "double")
        ])
    }

    static class ContourOptions extends RasterToVectorOptions {

        @Option(name = "-b", aliases = "--band", usage = "The band", required = false)
        int band = 0

        @Option(name = "-l", aliases = "--level", usage = "A level or interval", required = true)
        List<Double> levels

        @Option(name = "-s", aliases = "--simplify", usage = "Whether to simplify", required = false)
        boolean simplify

        @Option(name = "-m", aliases = "--smooth", usage = "Whether to smooth", required = false)
        boolean smooth

        @Option(name = "-n", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds
    }
}
