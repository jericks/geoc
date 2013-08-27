package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.geom.Geometry
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer

/**
 * Clip the input Layer by the other Layer to produce the output Layer.
 * @author Jared Erickson
 */
class ClipCommand extends LayerInOtherOutCommand<ClipOptions>{

    @Override
    String getName() {
        "vector clip"
    }

    @Override
    String getDescription() {
       "Clip the input Layer by the other Layer to produce the output Layer."
    }

    @Override
    ClipOptions getOptions() {
        new ClipOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, ClipOptions options, Reader reader, Writer writer) throws Exception {

        // Put all of the Features in the Clip Layer in a spatial index
        SpatialIndex index = new STRtree()
        otherLayer.eachFeature { f ->
            index.insert(f.bounds, f)
        }

        // Iterate through all of the Features in the input Layer
        inLayer.eachFeature(Filter.intersects(otherLayer.bounds.geometry), { f ->
            // See if the Feature intersects with the Bounds of any Feature in the spatial index
            index.query(f.bounds).each { clipFeature ->
                // Make sure it actually intersects the Geometry of a Feature in the spatial index
                if (f.geom.intersects(clipFeature.geom)) {
                    // Clip the geometry from the input Layer
                    Geometry intersection = f.geom.intersection(clipFeature.geom)
                    // Create a new Feature and add if to the clipped Layer
                    Map values = f.attributes
                    values[f.schema.geom.name] = intersection
                    outLayer.add(outLayer.schema.feature(values))
                }
            }
        })
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, ClipOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "clip", options), inputLayer.schema.fields)
    }

    static class ClipOptions extends LayerInOtherOutOptions {
    }
}
