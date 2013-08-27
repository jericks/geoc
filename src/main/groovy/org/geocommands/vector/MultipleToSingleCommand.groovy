package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

/**
 * Combine all of the geometries in the input Layer into one multipart geometry in the output Layer.
 * @author Jared Erickson
 */
class MultipleToSingleCommand extends LayerInOutCommand<MultipleToSingleOptions>{

    @Override
    String getName() {
        "vector multiple2single"
    }

    @Override
    String getDescription() {
        "Convert multipart geometries in the input Layer into single geometries in the output Layer."
    }

    @Override
    MultipleToSingleOptions getOptions() {
        new MultipleToSingleOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, MultipleToSingleOptions options, Reader reader, Writer writer) throws Exception {
        List geometries = []
        inLayer.eachFeature {f ->
            flattenGeometries(f.geom, geometries)
        }
        geometries.each{g ->
            outLayer.add([g])
        }
    }

    void flattenGeometries(Geometry g, List geometries) {
        if (g.numGeometries == 1) {
            geometries.add(g)
        } else {
            g.geometries.each {
                flattenGeometries(it, geometries)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, MultipleToSingleOptions options) {
        String geometryType = toSinglePart(layer.schema.geom.typ)
        new Schema(getOutputLayerName(layer,"single",options), [new Field(layer.schema.geom.name, geometryType, layer.schema.proj)])
    }

    private String toSinglePart(String geometryType) {
        if (geometryType.equalsIgnoreCase("MultiPoint")) {
            "Point"
        } else if (geometryType.equalsIgnoreCase("MultiLineString")) {
            "LineString"
        } else if (geometryType.equalsIgnoreCase("MultiPolygon")) {
            "Polygon"
        } else if (geometryType.equalsIgnoreCase("GeometryCollection")) {
            "Geometry"
        } else {
            geometryType
        }
    }

    static class MultipleToSingleOptions extends LayerInOutOptions {
    }
}
