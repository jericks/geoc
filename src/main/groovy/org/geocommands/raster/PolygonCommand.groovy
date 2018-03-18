package org.geocommands.raster

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.vector.Util
import org.kohsuke.args4j.Option

/**
 * Convert a Raster to a Polygon Vector Layer
 * @author Jared Erickson
 */
class PolygonCommand extends RasterToVectorCommand<PolygonOptions> {

    @Override
    String getName() {
        "raster polygon"
    }

    @Override
    String getDescription() {
        "Convert a Raster to a Polygon Vector Layer"
    }

    @Override
    PolygonOptions getOptions() {
        new PolygonOptions()
    }

    @Override
    void convertRasterToVector(Raster raster, Layer layer, PolygonOptions options, Reader reader, Writer writer) throws Exception {
        Map polyOptions = [
                band       : options.band,
                insideEdges: options.insideEdges,
                roi        : Geometry.fromString(options.regionOfInterest),
                noData     : options.noData,
                range      : options.ranges.collect { r ->
                    def parts = r.split(",")
                    [min: parts[0], minIncluded: parts[1], max: parts[2], maxIncluded: parts[3]]
                }
        ]
        Layer polygonLayer = raster.getPolygonLayer(polyOptions)
        layer.withWriter { geoscript.layer.Writer w ->
            polygonLayer.cursor.collect { f ->
                def newFeature = layer.schema.feature([the_geom: f.geom, value: f.get("value")], f.id)
                w.add(newFeature)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Raster raster, PolygonOptions options) {
        new Schema(Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, name.replaceAll(" ", "_")), [
                new Field("the_geom", "polygon"),
                new Field("value", "double")
        ])
    }

    static class PolygonOptions extends RasterToVectorOptions {

        @Option(name = "-b", aliases = "--band", usage = "The band", required = false)
        int band = 0

        @Option(name = "-e", aliases = "--inside-edges", usage = "Whether to include inside edges", required = false)
        boolean insideEdges

        @Option(name = "-g", aliases = "--region-of-interest", usage = "The region of interest", required = false)
        String regionOfInterest

        @Option(name = "-n", aliases = "--no-data", usage = "A no data value", required = false)
        List<String> noData

        @Option(name = "-a", aliases = "--range", usage = "A range (min,minIncluded,max,maxIncluded)", required = false)
        List<String> ranges
    }
}
