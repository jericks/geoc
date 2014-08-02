package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer

/**
 * Calculate the symmetric difference between two Layers..
 * @author Jared Erickson
 */
class SymDifferenceCommand extends LayerInOtherOutCombineSchemasCommand<SymDifferenceOptions> {

    @Override
    String getName() {
        "vector symdifference"
    }

    @Override
    String getDescription() {
        "Calculate the symmetric difference between two Layers."
    }

    @Override
    SymDifferenceOptions getOptions() {
        new SymDifferenceOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, SymDifferenceOptions options, Reader reader, Writer writer) throws Exception {

        Quadtree index = new Quadtree()
        inLayer.eachFeature { f ->
            Map features = [geom: f.geom, feature1: f, feature2: null]
            index.insert(features.geom.bounds, features)
        }

        otherLayer.eachFeature { f ->
            Geometry geom = f.geom
            index.query(f.geom.bounds).each { features ->
                if (geom.intersects(features.geom)) {
                    index.remove(features.geom.bounds, features)
                    Geometry difference1 = features.geom.difference(geom)
                    Geometry difference2 = geom.difference(features.geom)
                    geom = difference2
                    index.insert(difference1.bounds, [geom: difference1, feature1: features.feature1, feature2: null])
                }
            }
            index.insert(geom.bounds, [geom: geom, feature1: null, feature2: f])
        }

        outLayer.withWriter { geoscript.layer.Writer w ->
            Schema schema = outLayer.schema
            index.queryAll().each { features ->
                Geometry geom = features.geom
                Feature f1 = features.feature1
                Feature f2 = features.feature2
                Map attributes = [(schema.geom.name): geom]
                if (f1) {
                    Map fieldMap = options.fields[0]
                    f1.attributes.each { String k, Object v ->
                        if (!k.equalsIgnoreCase(inLayer.schema.geom.name) && fieldMap.containsKey(k)) {
                            attributes[fieldMap[k]] = v
                        }
                    }
                }
                if (f2) {
                    Map fieldMap = options.fields[1]
                    f2.attributes.each { String k, Object v ->
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

    static class SymDifferenceOptions extends LayerInOtherOutCombineSchemasOptions {
    }
}
