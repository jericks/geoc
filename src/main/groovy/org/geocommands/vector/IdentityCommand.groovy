package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer

/**
 * Calculate the identity between one Layer and another Layer.
 * @author Jared Erickson
 */
class IdentityCommand extends LayerInOtherOutCombineSchemasCommand<IdentityOptions>{

    @Override
    String getName() {
        "vector identity"
    }

    @Override
    String getDescription() {
        "Calculate the identity between one Layer and another Layer."
    }

    @Override
    IdentityOptions getOptions() {
        new IdentityOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, IdentityOptions options, Reader reader, Writer writer) throws Exception {

        // Add all Features from the first Layer into spatial index
        // Entries in the spatial index are Maps with geom, feature1, and feature2 values
        Quadtree index = new Quadtree()
        inLayer.eachFeature { f ->
            Map features = [geom: f.geom, feature1: f, feature2: null]
            index.insert(features.geom.bounds, features)
        }

        // Go through each Feature in the second Layer and check for intersections
        otherLayer.eachFeature { f ->
            // First, check the spatial index
            index.query(f.geom.bounds).each { features ->
                // Then make sure the geometries actually intersect
                if(f.geom.intersects(features.geom)) {
                    // Remove the original Feature
                    index.remove(features.geom.bounds, features)
                    // Calculate the intersection the difference
                    Geometry intersection = features.geom.intersection(f.geom)
                    Geometry difference = features.geom.difference(f.geom)

                    // Insert the intersection and difference back into the spatial index
                    index.insert(intersection.bounds, [geom: intersection, feature1: features.feature1, feature2: f])
                    index.insert(difference.bounds, [geom: difference, feature1: features.feature1, feature2: null])
                }
            }
        }

        // Put all Features in the spatial index into the output Layer
        Schema schema = outLayer.schema
        outLayer.withWriter {geoscript.layer.Writer w ->
            index.queryAll().each { features ->
                Geometry geom = features.geom
                Feature f1 = features.feature1
                Feature f2 = features.feature2
                Map attributes = [(schema.geom.name): geom]
                if (f1) {
                    Map fieldMap = options.fields[0]
                    f1.attributes.each {String k, Object v ->
                        if (!k.equalsIgnoreCase(inLayer.schema.geom.name) && fieldMap.containsKey(k)) {
                            attributes[fieldMap[k]] = v
                        }
                    }
                }
                if (f2) {
                    Map fieldMap = options.fields[1]
                    f2.attributes.each {String k, Object v ->
                        if (!k.equalsIgnoreCase(outLayer.schema.geom.name) && fieldMap.containsKey(k)) {
                            attributes[fieldMap[k]] = v
                        }
                    }
                }
                Feature f = schema.feature(attributes)
                w.add(f)
            }
        }
    }

    /*@Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, IdentityOptions options) {
        Map schemaAndFields = inputLayer.schema.addSchema(otherLayer.schema, getOutputLayerName(inputLayer, otherLayer, "identity", options),
            postfixAll: true, maxFieldNameLength: Util.isWorkspaceStringShapefile(options.outputWorkspace) ? 10 : -1)
        options.fields = schemaAndFields.fields
        schemaAndFields.schema
    }*/

    static class IdentityOptions extends LayerInOtherOutCombineSchemasOptions {
        //List fields
    }
}
