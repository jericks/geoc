package org.geocommands.raster

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.vector.Util

/**
 * Convert a Raster to a Point Vector Layer
 * @author Jared Erickson
 */
class PointCommand extends RasterToVectorCommand<PointOptions>{

    @Override
    String getName() {
        "raster point"
    }

    @Override
    String getDescription() {
        "Convert a Raster to a Point Vector Layer"
    }

    @Override
    PointOptions getOptions() {
        new PointOptions()
    }

    @Override
    void convertRasterToVector(Raster raster, Layer layer, PointOptions options, Reader reader, Writer writer) throws Exception {
        List rasterBands = raster.bands
        layer.withWriter {geoscript.layer.Writer w ->
            raster.pointLayer.cursor.collect{f ->
                Map values = [:]
                values["the_geom"] = f.geom
                rasterBands.eachWithIndex{b, i ->
                    def v = f.get(b.dim.description.toString())
                    if (v instanceof Byte) {
                        v = (v as Byte).doubleValue()
                    }
                    values["value${i}"] = v as double
                }
                def newFeature = layer.schema.feature(values, f.id)
                w.add(newFeature)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Raster raster, PointOptions options) {
        List<Field> fields = []
        fields.add(new Field("the_geom","point"))
        raster.bands.eachWithIndex{b, i ->
            fields.add(new Field("value${i}", "double"))
        }
        new Schema(Util.getOutputLayerName(options.outputWorkspace, options.outputLayer, name.replaceAll(" ","_")), fields)
    }

    static class PointOptions extends RasterToVectorOptions {
    }
}
