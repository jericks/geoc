package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Dissolve the Features of a Layer by a Field.
 * @author Jared Erickson
 */
class DissolveByFieldCommand extends LayerInOutCommand<DissolveByFieldOptions> {

    @Override
    String getName() {
        "vector dissolvebyfield"
    }

    @Override
    String getDescription() {
        "Dissolve the Features of a Layer by a Field."
    }

    @Override
    DissolveByFieldOptions getOptions() {
        new DissolveByFieldOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, DissolveByFieldOptions options, Reader reader, Writer writer) throws Exception {

        Field field = inLayer.schema.get(options.field)
        String idFieldName = options.idField
        String countFieldName = options.countField

        Map<Object, Geometry> values = [:]
        inLayer.eachFeature { f->
            Object value = f.get(field.name)
            if (!values.containsKey(value)) {
                values.put(value, [geom: f.geom, count: 1])
            } else {
                Map v = values.get(value)
                v.geom = v.geom.union(f.geom)
                v.count = v.count + 1
                values.put(value, v)
            }
        }

        String geomFieldName = outLayer.schema.geom.name
        values.eachWithIndex { value, i ->
            Map v = [:]
            v[idFieldName] = i
            v[field.name] = value.key
            v[countFieldName] = value.value.count
            v[geomFieldName] = value.value.geom
            outLayer.add(v)
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, DissolveByFieldOptions options) {
        Field fld = layer.schema.get(options.field)
        new Schema(getOutputLayerName(layer,"dissolve", options), [
                new Field(options.idField, "int"),
                new Field(options.countField, "int"),
                new Field(fld.name, fld.typ),
                new Field(layer.schema.geom)
        ])
    }

    static class DissolveByFieldOptions extends LayerInOutOptions {

        @Option(name="-f", aliases="--field",  usage="The field name", required = true)
        String field

        @Option(name="-d", aliases="--id-field",  usage="The id field name", required = false)
        String idField = "id"

        @Option(name="-c", aliases="--count-field",  usage="The count field name", required = false)
        String countField  = "count"

    }
}
