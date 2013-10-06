package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer

/**
 * Calculate the intersection between two Layers.
 * @author Jared Erickson
 */
class IntersectionCommand extends LayerInOtherOutCombineSchemasCommand<IntersectionOptions>{

    @Override
    String getName() {
        "vector intersection"
    }

    @Override
    String getDescription() {
        "Calculate the intersection between two Layers"
    }

    @Override
    IntersectionOptions getOptions() {
        new IntersectionOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, IntersectionOptions options, Reader reader, Writer writer) throws Exception {

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
                // Make sure the geometries actually intersect
                if(f.geom.intersects(features.geom)) {
                    // Calculate and insert the intersection
                    Geometry intersection = features.geom.intersection(f.geom)
                    index.insert(intersection.bounds, [geom: intersection, feature1: features.feature1, feature2: f])
                }
            }
        }

        // Only add features from the spatial index that have features from Layer 1 and Layer2
        Schema schema = outLayer.schema
        outLayer.withWriter {geoscript.layer.Writer w ->
            index.queryAll().each { features ->
                Geometry geom = features.geom
                Feature f1 = features.feature1
                Feature f2 = features.feature2
                if (f1 != null && f2 != null) {
                    Map attributes = [(schema.geom.name): geom]
                    Map fieldMap = options.fields[0]
                    f1.attributes.each {String k, Object v ->
                        if (!k.equalsIgnoreCase(inLayer.schema.geom.name) && fieldMap.containsKey(k)) {
                            attributes[fieldMap[k]] = v
                        }
                    }
                    fieldMap = options.fields[1]
                    f2.attributes.each {String k, Object v ->
                        if (!k.equalsIgnoreCase(otherLayer.schema.geom.name) && fieldMap.containsKey(k)) {
                            attributes[fieldMap[k]] = v
                        }
                    }
                    Feature f = schema.feature(attributes)
                    w.add(f)
                }
            }
        }
        
    }

    /*@Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, IntersectionOptions options) {
        Map schemaAndFields = inputLayer.schema.addSchema(otherLayer.schema, getOutputLayerName(inputLayer, otherLayer, "intersection", options),
            postfixAll: true, maxFieldNameLength: Util.isWorkspaceStringShapefile(options.outputWorkspace) ? 10 : -1)
        options.fields = schemaAndFields.fields
        schemaAndFields.schema
    }*/

    static class IntersectionOptions extends LayerInOtherOutCombineSchemasOptions {
        //List fields
    }
}
