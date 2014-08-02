package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.index.Quadtree
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Dissolve the intersecting Features of a Layer.
 * @author Jared Erickson
 */
class DissolveIntersectingCommand extends LayerInOutCommand<DissolveIntersectingOptions> {

    @Override
    String getName() {
        "vector dissolveintersecting"
    }

    @Override
    String getDescription() {
        "Dissolve the intersecting Features of a Layer."
    }

    @Override
    DissolveIntersectingOptions getOptions() {
        new DissolveIntersectingOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, DissolveIntersectingOptions options, Reader reader, Writer writer) throws Exception {

        Quadtree index = new Quadtree()
        inLayer.eachFeature { f ->
            Geometry unionGeom = f.geom
            int count = 1
            index.query(unionGeom.bounds).each { v ->
                Geometry g = v.geom
                if (unionGeom.intersects(g)) {
                    index.remove(g.bounds, v)
                    unionGeom = unionGeom.union(g)
                    count++
                }
            }
            index.insert(unionGeom.bounds, [geom: unionGeom, count: count])
        }

        String geomFieldName = outLayer.schema.geom.name
        outLayer.withWriter { geoscript.layer.Writer w ->
            int i = 0
            index.queryAll().each { v ->
                Map values = [:]
                values[options.idField] = i
                values[options.countField] = v.count
                values[geomFieldName] = v.geom
                w.add(outLayer.schema.feature(values))
                i++
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, DissolveIntersectingOptions options) {
        new Schema(getOutputLayerName(layer, "dissolve", options), [
                new Field(options.idField, "int"),
                new Field(options.countField, "int"),
                new Field(layer.schema.geom)
        ])
    }

    static class DissolveIntersectingOptions extends LayerInOutOptions {

        @Option(name = "-d", aliases = "--id-field", usage = "The id field name", required = false)
        String idField = "id"

        @Option(name = "-c", aliases = "--count-field", usage = "The count field name", required = false)
        String countField = "count"

    }
}
