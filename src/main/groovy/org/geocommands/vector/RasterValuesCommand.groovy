package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.raster.RasterUtil
import org.kohsuke.args4j.Option

/**
 * Get value from a Raster for each Feature's geometry
 * @author Jared Erickson
 */
class RasterValuesCommand extends LayerInOutCommand<RasterValuesOptions> {

    @Override
    String getName() {
        "vector raster values"
    }

    @Override
    String getDescription() {
        "Get value from a Raster for each Feature's geometry"
    }

    @Override
    RasterValuesOptions getOptions() {
        new RasterValuesOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, RasterValuesOptions options, Reader reader, Writer writer) throws Exception {
        Raster raster = RasterUtil.getRaster(options.inputRaster, options.inputProjection)
        List values = []
        inLayer.eachFeature { Feature f ->
            Point point = f.geom.centroid
            def value = null
            Map attributes = f.attributes
            if (raster.contains(point)) {
                value = raster.getValue(point, options.band, options.valueFieldType)
            }
            attributes[options.valueFieldName] = value
            values.add(attributes)
        }
        outLayer.add(values)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, RasterValuesOptions options) {
        layer.schema.addField(new Field(options.valueFieldName, options.valueFieldType), getOutputLayerName(layer, name, options))
    }

    static class RasterValuesOptions extends LayerInOutOptions {

        @Option(name = "-n", aliases = "--field-name", usage = "The new value field name (defaults to value)", required = false)
        String valueFieldName = "value"

        @Option(name = "-t", aliases = "--field-type", usage = "The new value field type (defaults to double)", required = false)
        String valueFieldType = "double"

        @Option(name = "-b", aliases = "--band", usage = "The band to get values from (defaults to 0)", required = false)
        int band = 0

        @Option(name = "-s", aliases = "--input-raster", usage = "The input raster", required = true)
        String inputRaster

        @Option(name = "-p", aliases = "--input-projection", usage = "The input projection", required = false)
        String inputProjection

    }
}
