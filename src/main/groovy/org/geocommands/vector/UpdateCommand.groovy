package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer

/**
 * Update one Layer with another.
 * @author Jared Erickson
 */
class UpdateCommand extends LayerInOtherOutCommand<UpdateOptions> {

    @Override
    String getName() {
        "vector update"
    }

    @Override
    String getDescription() {
        "Update one Layer with another Layer"
    }

    @Override
    UpdateOptions getOptions() {
        new UpdateOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, UpdateOptions options, Reader reader, Writer writer) throws Exception {

        // Add each Feature from the second layer to a spatial index
        Quadtree index = new Quadtree()
        otherLayer.eachFeature { f ->
            Map features = [geom: f.geom, feature1: null, feature2: f]
            index.insert(features.geom.bounds, features)
        }

        // Then go through each Feature from the first Layer and check
        // for intersections
        inLayer.eachFeature { f ->
            // Remember the Geometry, since there can be multiple intersecting features
            Geometry geom = f.geom
            // First check the spatial index
            index.query(geom.bounds).each { features ->
                // Then make sure the geometries actually intersect
                if (geom.intersects(features.geom)) {
                    // Calculate the difference
                    geom = geom.difference(features.geom)
                }
            }
            // Finally, insert the geometry into the spatial index
            index.insert(geom.bounds, [geom: geom, feature1: f, feature2: null])
        }

        // Add all Features in the spatial index to the output Layer
        outLayer.withWriter { geoscript.layer.Writer w ->
            Schema schema = outLayer.schema
            index.queryAll().each { features ->
                Geometry geom = features.geom
                Feature f1 = features.feature1
                Map attributes = [(schema.geom.name): geom]
                if (f1) {
                    f1.attributes.each { k, v ->
                        String fieldName = k as String
                        if (!fieldName.equalsIgnoreCase(inLayer.schema.geom.name)) {
                            attributes[k] = v
                        }
                    }
                }
                Feature f = schema.feature(attributes)
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, UpdateOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "update", options), inputLayer.schema.fields)
    }

    static class UpdateOptions extends LayerInOtherOutOptions {
    }
}
