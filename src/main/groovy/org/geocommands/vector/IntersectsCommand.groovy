package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer
import groovy.transform.CompileStatic

@CompileStatic
class IntersectsCommand extends LayerInOtherOutCommand<IntersectsOptions> {

    @Override
    String getName() {
        "vector intersects"
    }

    @Override
    String getDescription() {
        "Only include Features from the Input Layer that Intersect with Features from the Other Layer in the Output Layer."
    }

    @Override
    IntersectsOptions getOptions() {
        new IntersectsOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, IntersectsOptions options, Reader reader, Writer writer) throws Exception {

        // Put all of the Features in the Clip Layer in a spatial index
        SpatialIndex index = new STRtree()
        otherLayer.eachFeature { Feature f ->
            index.insert(f.bounds, f)
        }

        // Iterate through all of the Features in the input Layer
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature(Filter.intersects(otherLayer.bounds.geometry), { Feature f ->
                // See if the Feature intersects with the Bounds of any Feature in the spatial index
                index.query(f.bounds).each { def clipFeature ->
                    // Make sure it actually intersects the Geometry of a Feature in the spatial index
                    if (f.geom.intersects((clipFeature as Feature).geom)) {
                        w.add(outLayer.schema.feature(f.attributes))
                    }
                }
            })
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, IntersectsOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "intersects", options), inputLayer.schema.fields)
    }

    static class IntersectsOptions extends LayerInOtherOutOptions {
    }
}
