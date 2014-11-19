package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.geom.LineString
import geoscript.geom.MultiLineString
import geoscript.geom.Point
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

class SnapPointsToLinesCommand extends LayerInOtherOutCommand<SnapPointsToLinesOptions> {

    @Override
    String getName() {
        "vector snap points2lines"
    }

    @Override
    String getDescription() {
        "Snap points to their nearest line"
    }

    @Override
    SnapPointsToLinesOptions getOptions() {
        new SnapPointsToLinesOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, SnapPointsToLinesOptions options, Reader reader, Writer writer) throws Exception {
        Map layers = getInputs(inLayer, otherLayer)
        Layer pointLayer = layers.point
        Layer lineLayer = layers.line

        SpatialIndex index = new STRtree()
        lineLayer.eachFeature { Feature f ->
            index.insert(f.bounds, f)
        }

        outLayer.withWriter { geoscript.layer.Writer w ->
            pointLayer.eachFeature { Feature f ->
                Feature closestFeature
                double closestDistance = Double.MAX_VALUE
                index.query(f.geom.buffer(options.searchDistance).bounds).each { Feature lineFeature ->
                    double distance = f.geom.distance(lineFeature.geom)
                    if (distance < closestDistance) {
                        closestFeature = lineFeature
                        closestDistance = distance
                    }
                }
                Point snappedPoint = f.geom
                if (closestFeature) {
                    Geometry line = closestFeature.geom
                    if (line instanceof LineString) {
                        snappedPoint = line.placePoint(f.geom)
                    } else {
                        LineString ls = findClosest(f.geom, line as MultiLineString)
                        snappedPoint = ls.placePoint(f.geom)
                    }
                }
                Map values = f.attributes
                values[f.schema.geom.name] = snappedPoint
                values[options.snappedFieldName] = closestFeature ? 1 : 0
                w.add(outLayer.schema.feature(values))
            }
        }

    }

    private LineString findClosest(Geometry g, MultiLineString ml) {
        double closestDistance = Double.MAX_VALUE
        LineString closestLineString
        ml.geometries.each { LineString ls ->
            double distance = g.distance(ls)
            if (distance < closestDistance) {
                closestDistance = distance
                closestLineString = ls
            }
        }
        closestLineString
    }

    @Override
    protected Schema createOutputSchema(Layer inLayer, Layer otherLayer, SnapPointsToLinesOptions options) {
        Map layers = getInputs(inLayer, otherLayer)
        Layer pointLayer = layers.point
        pointLayer.schema.addField(
                new Field(options.snappedFieldName, "int"),
                getOutputLayerName(inLayer, otherLayer, "snapped", options)
        )
    }

    private Map<String, Layer> getInputs(Layer inLayer, Layer otherLayer) {
        Layer pointLayer
        Layer lineLayer
        if (isPoint(inLayer.schema.geom.typ)) {
            pointLayer = inLayer
        } else if (isPoint(otherLayer.schema.geom.typ)) {
            pointLayer = otherLayer
        }
        if (isLineString(inLayer.schema.geom.typ)) {
            lineLayer = inLayer
        } else if (isLineString(otherLayer.schema.geom.typ)) {
            lineLayer = otherLayer
        }
        if (!pointLayer) {
            throw new IllegalArgumentException("One of the input Layers must be a Point Layer!")
        }
        if (!lineLayer) {
            throw new IllegalArgumentException("One of the input Layers must be a Line Layer!")
        }
        [
                "point": pointLayer,
                "line" : lineLayer
        ]
    }

    private boolean isLineString(String geometryType) {
        geometryType.equalsIgnoreCase("linestring") || geometryType.equalsIgnoreCase("multilinestring")
    }

    private boolean isPoint(String geometryType) {
        geometryType.equalsIgnoreCase("point") || geometryType.equalsIgnoreCase("multipoint")
    }

    static class SnapPointsToLinesOptions extends LayerInOtherOutOptions {

        @Option(name = "-d", aliases = "--search-distance", usage = "The distance to search for the closest line", required = true)
        double searchDistance

        @Option(name = "-s", aliases = "--snapped-fieldname", usage = "The name for the snapped Field", required = false)
        String snappedFieldName = "SNAPPED"

    }

}
