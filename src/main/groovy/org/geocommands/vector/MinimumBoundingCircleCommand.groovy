package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer

/**
 * Calculate the minimum bounding circle of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class MinimumBoundingCircleCommand extends LayerInOutCommand<MinimumBoundingCircleOptions> {

    @Override
    String getName() {
        "vector mincircle"
    }

    @Override
    String getDescription() {
        "Calculate the minimum bounding circle of the input Layer and save them to the output Layer"
    }

    @Override
    MinimumBoundingCircleOptions getOptions() {
        new MinimumBoundingCircleOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, MinimumBoundingCircleOptions options) {
        new Schema(getOutputLayerName(layer, "mincircle", options), [new Field(layer.schema.geom.name, "Polygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, MinimumBoundingCircleOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature { f ->
            f.geom
        })
        outLayer.add([geoms.minimumBoundingCircle])
    }

    static class MinimumBoundingCircleOptions extends LayerInOutOptions {
    }
}
