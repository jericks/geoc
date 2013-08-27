package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

/**
 * Combine all of the geometries in the input Layer into one multipart geometry in the output Layer.
 * @author Jared Erickson
 */
class SingleToMultipleCommand extends LayerInOutCommand<SingleToMultipleOptions>{

    @Override
    String getName() {
        "vector single2multiple"
    }

    @Override
    String getDescription() {
        "Combine all of the geometries in the input Layer into one multipart geometry in the output Layer"
    }

    @Override
    SingleToMultipleOptions getOptions() {
        new SingleToMultipleOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, SingleToMultipleOptions options, Reader reader, Writer writer) throws Exception {
        GeometryCollection collection = new GeometryCollection(inLayer.collectFromFeature {f -> f.geom})
        outLayer.add([collection])
    }

    @Override
    protected Schema createOutputSchema(Layer layer, SingleToMultipleOptions options) {
        String geometryType = toMultiPart(layer.schema.geom.typ)
        new Schema(getOutputLayerName(layer,"multi",options), [new Field(layer.schema.geom.name, geometryType, layer.schema.proj)])
    }

    private String toMultiPart(String geometryType) {
        if (geometryType.equalsIgnoreCase("Point")) {
            "MultiPoint"
        } else if (geometryType.equalsIgnoreCase("LineString")) {
            "MultiLineString"
        } else if (geometryType.equalsIgnoreCase("Polygon")) {
            "MultiPolygon"
        } else if (geometryType.equalsIgnoreCase("Geometry")) {
            "GeometryCollection"
        } else {
            geometryType
        }
    }

    static class SingleToMultipleOptions extends LayerInOutOptions {
    }
}
