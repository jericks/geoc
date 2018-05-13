package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer
import groovy.transform.CompileStatic

@CompileStatic
class ContainsCommand extends LayerInOtherOutCommand<ContainsOptions> {

    @Override
    String getName() {
        "vector contains"
    }

    @Override
    String getDescription() {
        "Only include Features from the Input Layer that are contained by Features from the Other Layer in the Output Layer."
    }

    @Override
    ContainsOptions getOptions() {
        new ContainsOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, ContainsOptions options, Reader reader, Writer writer) throws Exception {

        // Put all of the Features in the Clip Layer in a spatial index
        SpatialIndex index = new STRtree()
        otherLayer.eachFeature { Feature f ->
            index.insert(f.bounds, f)
        }

        // Iterate through all of the Features in the input Layer
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature(Filter.intersects(otherLayer.bounds.geometry), { Feature inputFeature ->
                // See if the Feature intersects with the Bounds of any Feature in the spatial index
                index.query(inputFeature.bounds).each { def otherFeature ->
                    // Make sure it actually intersects the Geometry of a Feature in the spatial index
                    if (((otherFeature as Feature).geom).contains(inputFeature.geom)) {
                        w.add(outLayer.schema.feature(inputFeature.attributes))
                    }
                }
            })
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, ContainsOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "contains", options), inputLayer.schema.fields)
    }

    static class ContainsOptions extends LayerInOtherOutOptions {
    }
}
