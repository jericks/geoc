package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import org.kohsuke.args4j.Option
import org.locationtech.jts.algorithm.construct.LargestEmptyCircle

/**
 * A Command to get find the largest empty circle amound the features of the input Layer.
 * @author Jared Erickson
 */
class LargestEmptyCircleCommand extends LayerInOutCommand<LargestEmptyCircleOptions> {

    @Override
    String getName() {
        "vector largestemptycircle"
    }

    @Override
    String getDescription() {
        "find the largest empty circle amound the features of the input Layer"
    }

    @Override
    LargestEmptyCircleOptions getOptions() {
        new LargestEmptyCircleOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, LargestEmptyCircleOptions options, Reader reader, Writer writer) {
        def geoms = new GeometryCollection(inLayer.collectFromFeature { f ->
            f.geom
        })
        Geometry circle = geoms.getLargestEmptyCircle(options.tolerance)
        outLayer.add([circle])
    }

    @Override
    protected Schema createOutputSchema(Layer layer, LargestEmptyCircleOptions options) {
        new Schema(getOutputLayerName(layer, "circle", options), [new Field(layer.schema.geom.name, "Polygon", layer.schema.geom.proj)])
    }


    static class LargestEmptyCircleOptions extends LayerInOutOptions {

        @Option(name = "-t", aliases = "--tolerance", usage = "The tolerance", required = false)
        double tolerance = 1.0

    }
}
