package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.geom.LineString
import geoscript.geom.MultiLineString
import geoscript.geom.Point
import geoscript.layer.Layer
import org.kohsuke.args4j.Option
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.linearref.LengthIndexedLine

/**
 * A Command to create points along the lines of the input layer.
 * @author Jared Erickson
 */
class PointsAlongLineCommand extends LayerInOutCommand<PointsAlongLineOptions> {

    @Override
    String getName() {
        "vector pointsalongline"
    }

    @Override
    String getDescription() {
        "Create points along lines"
    }

    @Override
    PointsAlongLineOptions getOptions() {
        new PointsAlongLineOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, PointsAlongLineOptions options, Reader reader, Writer writer) {
        int id = 1
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                List<Point> points = []
                f.attributes.each { k, v ->
                    if (v instanceof Geometry) {
                        Geometry linearGeometry = v as Geometry
                        points.addAll(createPointsAlongGeometry(linearGeometry, options.distance))
                    } else {
                        values[k] = v
                    }
                }
                points.each { Point point ->
                    Map pointValues = [:]
                    pointValues.putAll(values)
                    pointValues.put(f.schema.geom.name, point)
                    id++
                    w.add(outLayer.schema.feature(pointValues, "${id}"))
                }
            }
        }
    }

    private List<Point> createPointsAlongGeometry(Geometry geometry, double distance) {
        if (geometry instanceof MultiLineString) {
            List<Point> points = []
            MultiLineString multiLineString = geometry as MultiLineString
            multiLineString.geometries.each { Geometry subGeometry ->
                points.addAll(createPointsAlongGeometry(subGeometry, distance))
            }
            points
        } else if (geometry instanceof LineString) {
            createPointsAlongLineString(geometry as LineString, distance)
        } else {
            throw new IllegalArgumentException("Unsupported Geometry Type!")
        }
    }

    private List<Point> createPointsAlongLineString(LineString lineString, double distance) {
        double lineLength = lineString.length
        LengthIndexedLine lengthIndexedLine = new LengthIndexedLine(lineString.g)

        List<Point> points = []
        points.add(lineString.startPoint)
        double distanceAlongLine = distance
        while(distanceAlongLine < lineLength) {
            Coordinate coordinate = lengthIndexedLine.extractPoint(distanceAlongLine)
            points.add(new Point(coordinate.x, coordinate.y))
            distanceAlongLine = distanceAlongLine + distance
        }

        points
    }

    @Override
    protected Schema createOutputSchema(Layer layer, PointsAlongLineOptions options) {
        layer.schema.changeGeometryType("Point", getOutputLayerName(layer, "points", options))
    }

    static class PointsAlongLineOptions extends LayerInOutOptions {

        @Option(name = "-d", aliases = "--distance", usage = "The distance between points", required = true)
        double distance

    }
}
