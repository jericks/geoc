package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

/**
 *
 */
class MinimumRectangleCommand extends LayerInOutCommand<MinimumRectangleOptions> {

    @Override
    String getName() {
        "vector minrect"
    }

    @Override
    String getDescription() {
        "Calculate the minimum rectangle of the input Layer and save it to the output Layer"
    }

    @Override
    MinimumRectangleOptions getOptions() {
        new MinimumRectangleOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, MinimumRectangleOptions options) {
        new Schema(getOutputLayerName(layer, "minrect", options), [new Field(layer.schema.geom.name, "Polygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, MinimumRectangleOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature {f ->
            f.geom
        })
        outLayer.add([geoms.minimumRectangle])
    }

    static class MinimumRectangleOptions extends LayerInOutOptions {
    }
}
