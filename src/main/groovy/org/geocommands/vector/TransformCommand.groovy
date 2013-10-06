package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.workspace.Memory
import org.kohsuke.args4j.Option

/**
 * Transform the values of the input Layer using Expression and Functions
 * @author Jared Erickson
 */
class TransformCommand extends LayerInOutCommand<TransformOptions>{

    @Override
    String getName() {
        "vector transform"
    }

    @Override
    String getDescription() {
        "Transform the values of the input Layer using Expression and Functions"
    }

    @Override
    TransformOptions getOptions() {
        new TransformOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, TransformOptions options, Reader reader, Writer writer) throws Exception {
        Layer transformedLayer = inLayer.transform(outLayer.name, options.definitions)
        outLayer.withWriter {geoscript.layer.Writer w ->
            transformedLayer.eachFeature {f ->
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, TransformOptions options) {
        // Since the geometry can be transformed, we need to see what geometry type we are ending up with
        // so put the first feature in to a Memory Layer
        Memory mem = new Memory()
        Layer oneFeatureLayer = mem.create(layer.name + "_firstfeature", layer.schema.fields)
        oneFeatureLayer.add(layer.first())
        // And then transform it to find the geometry type
        Layer transformedLayer = oneFeatureLayer.transform(layer.name + "_firstfeature_transformed", options.definitions)
        String geomType = transformedLayer.first().geom.geometryType
        // And then use the geometry type to create the correct schema
        layer.schema.changeGeometryType(geomType, getOutputLayerName(layer, "transformed", options))
    }

    static class TransformOptions extends LayerInOutOptions {
        @Option(name="-d", aliases="--definition",  usage="A transform definition 'field=expression'", required = true)
        Map<String,String> definitions
    }
}
