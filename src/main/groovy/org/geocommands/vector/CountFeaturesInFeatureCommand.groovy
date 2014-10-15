package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Count the number of features in each feature
 * @author Jared Erickson
 */
class CountFeaturesInFeatureCommand extends LayerInOtherOutCommand<CountFeaturesInFeatureOptions> {

    @Override
    String getName() {
        "vector count featuresInfeature"
    }

    @Override
    String getDescription() {
        "Count the number of features in a feature"
    }

    @Override
    CountFeaturesInFeatureOptions getOptions() {
        new CountFeaturesInFeatureOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, CountFeaturesInFeatureOptions options, Reader reader, Writer writer) throws Exception {
        Map layers = getPolygonAndOtherLayers(inLayer, otherLayer)
        Layer polygonLayer = layers.polygon
        Layer nonPolygonLayer = layers.other
        outLayer.withWriter { geoscript.layer.Writer w ->
            polygonLayer.eachFeature { Feature f ->
                int count = nonPolygonLayer.count(Filter.intersects(f.geom))
                Map attributes = f.attributes
                attributes[options.countFieldName] = count
                w.add(outLayer.schema.feature(attributes, f.id))
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inLayer, Layer otherLayer, CountFeaturesInFeatureOptions options) {
        Map layers = getPolygonAndOtherLayers(inLayer, otherLayer)
        Layer polygonLayer = layers.polygon
        Layer nonPolygonLayer = layers.other
        polygonLayer.schema.addField(new Field(options.countFieldName, "int"), getOutputLayerName(polygonLayer, nonPolygonLayer, "count", options))
    }

    private Map getPolygonAndOtherLayers(Layer inLayer, Layer otherLayer) {
        Layer polygonLayer
        Layer nonPolygonLayer
        if (isPolygon(inLayer.schema.geom.typ)) {
            polygonLayer = inLayer
            nonPolygonLayer = otherLayer
        } else if (isPolygon(otherLayer.schema.geom.typ)) {
            polygonLayer = otherLayer
            nonPolygonLayer = inLayer
        } else {
            throw new IllegalArgumentException("One of the input Layers must contain Polygon Geometry!")
        }
        [
                "polygon": polygonLayer,
                "other": nonPolygonLayer
        ]
    }

    private boolean isPolygon(String geometryType) {
        geometryType.equalsIgnoreCase("polygon") || geometryType.equalsIgnoreCase("multipolygon")
    }

    static class CountFeaturesInFeatureOptions extends LayerInOtherOutOptions {
        @Option(name = "-f", aliases = "--count-fieldname", usage = "The name for the count Field", required = false)
        String countFieldName = "count"
    }

}
