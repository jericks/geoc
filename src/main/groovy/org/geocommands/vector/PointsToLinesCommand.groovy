package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.filter.Filter
import geoscript.geom.LineString
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Convert a Layer with Points into a Layer with Lines.
 * @author Jared Erickson
 */
class PointsToLinesCommand extends LayerInOutCommand<PointsToLineOptions>{

    @Override
    String getName() {
        "vector points2lines"
    }

    @Override
    String getDescription() {
        "Convert points to lines"
    }

    @Override
    PointsToLineOptions getOptions() {
        new PointsToLineOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, PointsToLineOptions options, Reader reader, Writer writer) throws Exception {
        if (!inLayer.schema.geom.typ.toUpperCase() in ["POINT","MULTIPOINT"]) {
            throw new IllegalArgumentException("The input Layer must contain Point or MultiPoint geoemtries!")
        }
        if (!outLayer.schema.geom.typ.toUpperCase() in ["LINESTRING","MULTILINESTRING"]) {
            throw new IllegalArgumentException("The input Layer must contain LineString or MultiLineString geoemtries!")
        }

        List sortFields = []
        if (options.groupByField) {
            sortFields.add(options.groupByField)
        }
        if (options.orderByField) {
            sortFields.add(options.orderByField)
        }

        outLayer.withWriter { geoscript.layer.Writer w ->
            Object group = null
            List points = []
            int id = 1
            inLayer.getCursor(Filter.PASS, sortFields).each { Feature f ->
                Object currentGroup = null
                if (options.groupByField) {
                    currentGroup = f.get(options.groupByField)
                }
                if (!group.equals(currentGroup)) {
                    if (points.size() > 1) {
                        Feature newFeature = w.newFeature
                        newFeature["id"] = id
                        newFeature["group"] = group
                        newFeature.geom = new LineString(points)
                        w.add(newFeature)
                        points = []
                        id = id + 1
                    }
                    group = currentGroup
                }
                points.addAll(f.geom.points)
            }
            if (points.size() > 1) {
                Feature newFeature = w.newFeature
                newFeature["id"] = id
                if (options.groupByField) {
                    newFeature["group"] = group
                }
                newFeature.geom = new LineString(points)
                w.add(newFeature)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, PointsToLineOptions options) {
        List fields = [
                new Field("id","int"),
                new Field("the_geom", "linestring", layer.schema.geom.proj)
        ]
        if (options.groupByField) {
            fields.add(new Field("group", layer.schema.get(options.groupByField).typ))
        }
        new Schema(getOutputLayerName(layer, "lines", options), fields)
    }

    static class PointsToLineOptions extends LayerInOutOptions {

        @Option(name = "-s", aliases = "--sort-field", usage = "The Field to sort the field", required = false)
        String orderByField

        @Option(name = "-g", aliases = "--group-field", usage = "The Field used create separate Lines", required = false)
        String groupByField

    }
}
