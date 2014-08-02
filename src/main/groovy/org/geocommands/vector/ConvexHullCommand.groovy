package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer

/**
 * Calculate the convex hull of the input Layer and save it to the output Layer
 * @author Jared Erickson
 */
class ConvexHullCommand extends LayerInOutCommand<LayerInOutOptions> {

    @Override
    String getName() {
        "vector convexhull"
    }

    @Override
    String getDescription() {
        "Calculate the convex hull of the input Layer and save it to the output Layer"
    }

    @Override
    LayerInOutOptions getOptions() {
        new LayerInOutOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, LayerInOutOptions options) {
        new Schema(getOutputLayerName(layer, "convexhull", options), [new Field(layer.schema.geom.name, "Polygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, LayerInOutOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature {f ->
            f.geom
        })
        outLayer.add([geoms.convexHull])
    }
}
