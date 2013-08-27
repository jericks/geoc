package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 *
 */
class ConvexHullsCommand extends LayerInOutCommand<ConvexHullsOptions> {

    @Override
    String getName() {
        "vector convexhulls"
    }

    @Override
    String getDescription() {
        "Calculate the convex hull of each feature in the input Layer and save them to the output Layer"
    }

    @Override
    ConvexHullsOptions getOptions() {
        new ConvexHullsOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, ConvexHullsOptions options) {
        layer.schema.changeGeometryType("Polygon", getOutputLayerName(layer, "convexhulls", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, ConvexHullsOptions options, Reader reader, Writer writer) {
        inLayer.eachFeature {Feature f ->
            Map values = [:]
            f.attributes.each{k,v ->
                if (v instanceof geoscript.geom.Geometry) {
                    values[k] = v.convexHull
                } else {
                    values[k] = v
                }
            }
            outLayer.add(values)
        }
    }

    static class ConvexHullsOptions extends LayerInOutOptions {
    }
}
