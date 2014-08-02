package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer

/**
 * Erase features from one Layer based on another Layer.
 * @author Jared Erickson
 */
class EraseCommand extends LayerInOtherOutCommand<EraseOptions> {

    @Override
    String getName() {
        "vector erase"
    }

    @Override
    String getDescription() {
        "Erase features from one Layer based on another Layer"
    }

    @Override
    EraseOptions getOptions() {
        new EraseOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, EraseOptions options, Reader reader, Writer writer) throws Exception {

        // Add each Feature from the first Layer to a spatial index
        Quadtree index = new Quadtree()
        inLayer.eachFeature { f ->
            index.insert(f.geom.bounds, f)
        }

        // Go through each Feature from the second Layer see if
        // it intersects with any Feature from the first layer
        otherLayer.eachFeature { f2 ->
            // First check the spatial index
            index.query(f2.geom.bounds).each { f1 ->
                // Then make sure the geometries actually intersect
                if (f1.geom.intersects(f2.geom)) {
                    // Remove the original Feature
                    index.remove(f1.geom.bounds, f1)
                    // Calculate the difference
                    Geometry difference = f1.geom.difference(f2.geom)
                    f1.geom = difference
                    // Insert the Feature with the new Geometry
                    index.insert(difference.bounds, f1)
                }
            }
        }

        // Add all Features in the spatial index to the output Layer
        outLayer.withWriter { geoscript.layer.Writer w ->
            index.queryAll().each { f ->
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, EraseOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "erase", options), inputLayer.schema.fields)
    }

    static class EraseOptions extends LayerInOtherOutOptions {
    }
}
