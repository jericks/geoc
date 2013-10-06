package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOtherOutCombineSchemasCommand
import org.geocommands.vector.LayerInOtherOutCombineSchemasOptions

/**
 * Union one Layer with another.
 * @author Jared Erickson
 */
class UnionCommand extends LayerInOtherOutCombineSchemasCommand<UnionOptions>{

    @Override
    String getName() {
        "vector union"
    }

    @Override
    String getDescription() {
        "Union one Layer with another Layer"
    }

    @Override
    UnionOptions getOptions() {
        new UnionOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, UnionOptions options, Reader reader, Writer writer) throws Exception {

        // Add all Features from the first Layer into spatial index
        // Entries in the spatial index are Maps with geom, feature1, and feature2 values
        Quadtree index = new Quadtree()
        inLayer.eachFeature { f ->
            Map features = [geom: f.geom, feature1: f, feature2: null]
            index.insert(features.geom.bounds, features)
        }

        // Go through each Feature in the second Layer
        otherLayer.eachFeature { f ->
            Geometry geom = f.geom
            // Check the spatial index to see if this Feature intersects anything
            index.query(f.geom.bounds).each { features ->
                // Make sure the Geometries actually intersect
                if(geom.intersects(features.geom)) {
                    // Remove the original Feaure
                    index.remove(features.geom.bounds, features)
                    // Calculate the intersection and difference from both sides
                    Geometry intersection = features.geom.intersection(geom)
                    Geometry difference1 = features.geom.difference(geom)
                    Geometry difference2 = geom.difference(features.geom)
                    // Store the second difference because more Features may intersect it
                    geom = difference2
                    // Insert the first difference and the intersection into the spatial index
                    index.insert(intersection.bounds, [geom: intersection, feature1: features.feature1, feature2: f])
                    index.insert(difference1.bounds, [geom: difference1, feature1: features.feature1, feature2: null])
                }
            }
            // Finally, insert geometry from the second Layer
            index.insert(geom.bounds, [geom: geom, feature1: null, feature2: f])
        }

        // Put all Features in the spatial index into the output Layer
        outLayer.withWriter {geoscript.layer.Writer w ->
            Schema schema = outLayer.schema
            index.queryAll().each { features ->
                Geometry geom = features.geom
                Feature f1 = features.feature1
                Feature f2 = features.feature2
                Map attributes = [:]
                attributes[schema.geom.name] = geom
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
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, UnionOptions options) {
        Map schemaAndFields = inputLayer.schema.addSchema(otherLayer.schema, getOutputLayerName(inputLayer, otherLayer, "union", options),
            postfixAll: true, maxFieldNameLength: Util.isWorkspaceStringShapefile(options.outputWorkspace) ? 10 : -1)
        options.fields = schemaAndFields.fields
        schemaAndFields.schema
    }*/

    static class UnionOptions extends LayerInOtherOutCombineSchemasOptions {
        //List fields
    }
}
