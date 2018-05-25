package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer
import groovy.transform.CompileStatic
import org.kohsuke.args4j.Option

@CompileStatic
class DistanceWithinCommand extends LayerInOtherOutCommand<DistanceWithinOptions> {

    @Override
    String getName() {
        "vector distancewithin"
    }

    @Override
    String getDescription() {
        "Only include Features from the Input Layer that are within a given distance of Features from the Other Layer in the Output Layer."
    }

    @Override
    DistanceWithinOptions getOptions() {
        new DistanceWithinOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, DistanceWithinOptions options, Reader reader, Writer writer) throws Exception {

        // Put all of the Features in the Other Layer in a spatial index
        SpatialIndex index = new STRtree()
        otherLayer.eachFeature { Feature f ->
            index.insert(f.bounds.geometry.buffer(options.distance).bounds, f)
        }

        // Iterate through all of the Features in the input Layer
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature inputFeature ->
                // See if the Feature intersects with the Bounds of any Feature in the spatial index
                index.query(inputFeature.bounds).each { def otherFeature ->
                    // Make sure it actually within the distance of a Feature in the spatial index
                    if (((otherFeature as Feature).geom).isWithinDistance(inputFeature.geom, options.distance)) {
                        w.add(outLayer.schema.feature(inputFeature.attributes))
                    }
                }
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, DistanceWithinOptions options) {
        new Schema(getOutputLayerName(inputLayer, otherLayer, "contains", options), inputLayer.schema.fields)
    }

    static class DistanceWithinOptions extends LayerInOtherOutOptions {

        @Option(name = "-d", aliases = "--distance", usage = "The distance", required = true)
        double distance

    }
}
