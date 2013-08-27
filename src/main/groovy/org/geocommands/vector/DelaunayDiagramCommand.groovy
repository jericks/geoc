package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer

/**
 * Calculate a delaunay triangle diagram of the input Layer and save it to the output Layer
 * @author Jared Erickson
 */
class DelaunayDiagramCommand extends LayerInOutCommand<DelaunayDiagramOptions> {

    @Override
    String getName() {
        "vector delaunay"
    }

    @Override
    String getDescription() {
        "Calculate a delaunay triangle diagram of the input Layer and save it to the output Layer"
    }

    @Override
    DelaunayDiagramOptions getOptions() {
        new DelaunayDiagramOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, DelaunayDiagramOptions options) {
        new Schema(getOutputLayerName(layer, "delaunay", options), [new Field(layer.schema.geom.name, "MultiPolygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, DelaunayDiagramOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature {f ->
            f.geom
        })
        outLayer.add([geoms.delaunayTriangleDiagram])
    }

    static class DelaunayDiagramOptions extends LayerInOutOptions{
    }
}
