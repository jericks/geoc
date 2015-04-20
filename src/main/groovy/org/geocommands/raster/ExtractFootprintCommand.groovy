package org.geocommands.raster

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.Raster
import geoscript.layer.Writer as LayerWriter
import org.geocommands.vector.Util
import org.kohsuke.args4j.Option

/**
 * Extract the footprint of the Raster as a Vector Layer
 * @author Jared Erickson
 */
class ExtractFootprintCommand extends RasterToVectorCommand<ExtractFootprintOptions> {

    @Override
    String getName() {
        "raster extractfootprint"
    }

    @Override
    String getDescription() {
        "Extract the footprint of the Raster as a Vector Layer"
    }

    @Override
    ExtractFootprintOptions getOptions() {
        new ExtractFootprintOptions()
    }

    @Override
    protected Schema createOutputSchema(Raster raster, ExtractFootprintOptions options) {
        String type = raster.bands[0].type
        if (type.equalsIgnoreCase("byte")) {
            type = "String"
        }
        new Schema(Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, name.replaceAll(" ", "_")), [
                new Field("the_geom", "polygon"),
                new Field("value", type)
        ])
    }

    @Override
    void convertRasterToVector(Raster raster, Layer layer, ExtractFootprintOptions options, Reader reader, Writer writer) throws Exception {
        layer.withWriter { LayerWriter layerWriter ->
            raster.extractFootPrint(
                    exclusionRanges: options.exclusionRanges.collect { String range ->
                        List values = range.split(",")
                        [min: values[0], max: values[1]]
                    },
                    thresholdArea: options.thresholdArea,
                    computeSimplifiedFootprint: options.computeSimplifiedFootprint,
                    simplifierFactor: options.simplifierFactor,
                    removeCollinear: options.removeCollinear,
                    forceValid: options.forceValid,
                    loadingType: options.loadingType
            ).eachFeature {Feature f ->
                layerWriter.add(f)
            }
        }
    }

    static class ExtractFootprintOptions extends RasterToVectorOptions {

        @Option(name = "-e", aliases = "--exclusion-range", usage = "A comma delimited range of values to exclude from the search.", required = false)
        List<String> exclusionRanges = []

        @Option(name = "-t", aliases = "--threshold-area", usage = "A number used to exclude small Polygons.  The default is 5.", required = false)
        double thresholdArea = 5

        @Option(name = "-f", aliases = "--compute-simplified-footprint", usage = "Whether to compute a simplified footprint or not.  The default is false.", required = false)
        boolean computeSimplifiedFootprint = false

        @Option(name = "-s", aliases = "--simplifier-factor", usage = "A number used to simplify the geometry. The default is 2.", required = false)
        double simplifierFactor = 2

        @Option(name = "-c", aliases = "--remove-collinear", usage = "Whether to remove collinear coordinates. The default is true.", required = false)
        boolean removeCollinear = true

        @Option(name = "-v", aliases = "--force-valid", usage = "Whether to force creation of valid polygons.  The default is true.", required = false)
        boolean forceValid = true

        @Option(name = "-y", aliases = "--loading-type", usage = "The image loading type (Deferred or Immediate). Immediate is the default.", required = false)
        String loadingType = "immediate"

    }
}
