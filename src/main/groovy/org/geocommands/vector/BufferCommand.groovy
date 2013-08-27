package org.geocommands.vector

import geoscript.filter.Expression
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.feature.Schema
import org.kohsuke.args4j.Option
import geoscript.feature.Feature

/**
 * Buffer the features of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class BufferCommand extends LayerInOutCommand<BufferOptions> {

    @Override
    String getName() {
        "vector buffer"
    }

    @Override
    String getDescription() {
        "Buffer the features of the input Layer and save them to the output Layer"
    }

    @Override
    BufferOptions getOptions() {
        new BufferOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, BufferOptions options, Reader reader, Writer writer) {

        Expression distance = Expression.fromCQL(options.distance)

        int capStyle = Geometry.CAP_ROUND
        if (options.capStyle.equalsIgnoreCase("round")) {
            capStyle = Geometry.CAP_ROUND
        } else if (options.capStyle.equalsIgnoreCase("butt")) {
            capStyle = Geometry.CAP_BUTT
        } else if (options.capStyle.equalsIgnoreCase("square")) {
            capStyle = Geometry.CAP_SQUARE
        }

        inLayer.eachFeature {Feature f ->
            Map values = [:]
            f.attributes.each{k,v ->
                if (v instanceof geoscript.geom.Geometry) {
                    double d = distance.evaluate(f) as double
                    Geometry b = options.singleSided ?
                        v.buffer(d, options.quadrantSegments, capStyle) :
                        v.singleSidedBuffer(d, options.quadrantSegments, capStyle)
                    values[k] = b
                } else {
                    values[k] = v
                }
            }
            outLayer.add(values)
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, LayerInOutOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "buffer", options))
    }

    static class BufferOptions extends LayerInOutOptions {

        @Option(name="-d", aliases="--distance",  usage="The buffer distance", required = true)
        String distance

        @Option(name="-q", aliases="--quadrantsegments",  usage="The number of quadrant segments", required = false)
        int quadrantSegments = 8

        @Option(name="-s", aliases="--singlesided",  usage="Whether buffer should be single sided or not", required = false)
        boolean singleSided = false

        @Option(name="-c", aliases="--capstyle",  usage="The cap style", required = false)
        String capStyle = "round"
    }
}
