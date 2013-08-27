package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import org.geocommands.vector.LayerInOutCommand
import org.geocommands.vector.LayerInOutOptions

/**
 *
 */
class VoronoiDiagramCommand extends LayerInOutCommand<VoronoiDiagramOptions> {

    @Override
    String getName() {
        "vector voronoi"
    }

    @Override
    String getDescription() {
        "Calculate a voronoi diagram of the input Layer and save it to the output Layer"
    }

    @Override
    VoronoiDiagramOptions getOptions() {
        new VoronoiDiagramOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, VoronoiDiagramOptions options) {
        new Schema(getOutputLayerName(layer, "voronoi", options), [new Field(layer.schema.geom.name, "MultiPolygon", layer.schema.geom.proj)])
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, VoronoiDiagramOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature {f ->
            f.geom
        })
        outLayer.add([geoms.voronoiDiagram])
    }

    static class VoronoiDiagramOptions extends LayerInOutOptions {
    }
}
