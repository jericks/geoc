package org.geocommands.vector

import geoscript.layer.Layer
import org.geocommands.vector.LayerInOtherOutCommand
import org.geocommands.vector.LayerInOtherOutOptions

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
        inLayer.eachFeature{ f->
            outLayer.add(f)
        }
        otherLayer.eachFeature{ f->
            outLayer.add(f)
        }
    }

    static class MergeOptions extends LayerInOtherOutOptions {
    }
}
