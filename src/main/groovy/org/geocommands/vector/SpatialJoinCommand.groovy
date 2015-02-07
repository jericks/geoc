package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer
import org.geocommands.vector.SpatialJoinCommand.SpatialJoinOptions
import org.kohsuke.args4j.Option

/**
 * Spatially join two layers to create the output Layer.
 * @author Jared Erickson
 */
class SpatialJoinCommand extends LayerInOtherOutCommand<SpatialJoinOptions> {

    @Override
    String getName() {
        "vector join spatial"
    }

    @Override
    String getDescription() {
        "Spatially join two layers to create the output Layer."
    }

    @Override
    SpatialJoinOptions getOptions() {
        new SpatialJoinOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, SpatialJoinOptions options, Reader reader, Writer writer) throws Exception {

        Quadtree index = new Quadtree()
        otherLayer.eachFeature { Feature f ->
            index.insert(f.geom.bounds, f)
        }

        outLayer.withWriter { geoscript.layer.Writer layerWriter ->
            inLayer.eachFeature { Feature f ->
                Feature newFeature = layerWriter.newFeature
                Map values = f.attributes
                // See if the Feature intersects with the Bounds of any Feature in the spatial index
                List hits = index.query(f.bounds)
                if (hits.size() > 0) {
                    if (options.multipleType.equalsIgnoreCase("first")) {
                        Feature indexFeature = hits[0]
                        if (matches(options.spatialType, f, indexFeature)) {
                            options.fields.each { String fieldName ->
                                values[fieldName] = indexFeature[fieldName]
                            }
                        }
                    } else if (options.multipleType.equalsIgnoreCase("closest")) {
                        double distance = Double.MAX_VALUE
                        Feature closestFeature = null
                        hits.each { Feature indexFeature ->
                            if (matches(options.spatialType, f, indexFeature)) {
                                double d = f.geom.centroid.distance(indexFeature.geom.centroid)
                                if (d < distance) {
                                    distance = d
                                    closestFeature = indexFeature
                                }
                            }
                        }
                        if (closestFeature) {
                            options.fields.each { String fieldName ->
                                values[fieldName] = closestFeature[fieldName]
                            }
                        }
                    }  else if (options.multipleType.equalsIgnoreCase("largest")) {
                        double largestSize = Double.MIN_VALUE
                        Feature largestFeature = null
                        hits.each { Feature indexFeature ->
                            if (matches(options.spatialType, f, indexFeature)) {
                                Geometry intersection = f.geom.intersection(indexFeature)
                                double size
                                if (intersection.geometryType in ['Polygon','MultiPolygon']) {
                                    size = intersection.area
                                } else {
                                    size = intersection.length
                                }
                                if (size > largestSize) {
                                    largestSize = size
                                    largestFeature = indexFeature
                                }
                            }
                        }
                        if (largestFeature) {
                            options.fields.each { String fieldName ->
                                values[fieldName] = largestFeature[fieldName]
                            }
                        }
                    }

                }
                newFeature.set(values)
                layerWriter.add(newFeature)
            }

        }
    }

    protected boolean matches(String type, Feature inFeature, Feature otherFeature) {
        boolean matches = false
        if (options.spatialType.equalsIgnoreCase("contains")) {
            matches = otherFeature.geom.contains(inFeature.geom)
        } else /*if (options.spatialType.equalsIgnoreCase("contains"))*/ {
            matches = otherFeature.geom.intersects(inFeature.geom)
        }
        matches
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, SpatialJoinOptions options) {
        List fields = []
        otherLayer.schema.fields.each { Field fld ->
            if (options.fields.contains(fld.name)) {
                fields.add(fld)
            }
        }
        inputLayer.schema.addFields(fields, getOutputLayerName(inputLayer, otherLayer, name, options))
    }

    static class SpatialJoinOptions extends LayerInOtherOutOptions {

        @Option(name = "-f", aliases = "--field", usage = "A Field name", required = true)
        List<String> fields

        @Option(name = "-t", aliases = "--spatial-type", usage = "The spatial type (intersects, contains). Defaults to intersects.", required = false)
        String spatialType = "intersects"

        @Option(name = "-m", aliases = "--multiple-type", usage = "The multiple type (first, closest, largest). Defaults to first.", required = false)
        String multipleType = "first"

    }

}
