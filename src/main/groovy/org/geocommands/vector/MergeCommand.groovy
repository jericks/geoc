package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer

/**
 * Merge two Layers together to create a new Layer
 * @author Jared Erickson
 */
class MergeCommand extends LayerInOtherOutCommand<MergeOptions> {

    @Override
    String getName() {
        "vector merge"
    }

    @Override
    String getDescription() {
        "Merge two Layers together to create a new Layer"
    }

    @Override
    MergeOptions getOptions() {
        new MergeOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, MergeOptions options, Reader reader, Writer writer) throws Exception {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                w.add(f)
            }
            otherLayer.eachFeature { Feature f ->
                w.add(f)
            }
        }
    }

    static class MergeOptions extends LayerInOtherOutOptions {
    }
}
