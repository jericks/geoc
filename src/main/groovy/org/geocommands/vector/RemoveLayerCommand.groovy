package org.geocommands.vector

import geoscript.layer.Layer

/**
 * Remove a Layer from a Workspace
 * @author Jared Erickson
 */
class RemoveLayerCommand extends LayerCommand<RemoveLayerOptions>{

    @Override
    String getName() {
        "vector remove layer"
    }

    @Override
    String getDescription() {
        "Remove a Layer from a Workspace"
    }

    @Override
    RemoveLayerOptions getOptions() {
        new RemoveLayerOptions()
    }

    @Override
    protected void processLayer(Layer layer, RemoveLayerOptions options, Reader reader, Writer writer) throws Exception {
        layer.workspace.remove(layer)
    }

    static class RemoveLayerOptions extends LayerOptions {
    }

}
