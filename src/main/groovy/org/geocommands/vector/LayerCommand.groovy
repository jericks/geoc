package org.geocommands.vector

import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import org.geocommands.Command

/**
 * The LayerCommand is a Command that operates on a Layer.
 * @author Jared Erickson
 */
abstract class LayerCommand<T extends LayerOptions> extends Command<T> {

    abstract String getName()
    abstract String getDescription()
    abstract T getOptions()

    void execute(T options, Reader reader, Writer writer) throws Exception {
        Layer layer = getInputLayer(options.inputWorkspace, options.inputLayer, reader)
        try {
            processLayer(layer, options, reader, writer)
            if (shouldWriteLayer() && layer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(layer))
            }
        }
        finally {
            layer.workspace.close()
        }
    }

    protected abstract void processLayer(Layer layer, T options, Reader reader, Writer writer) throws Exception;

    protected boolean shouldWriteLayer() {
        false
    }

    Layer getInputLayer(String workspaceStr, String layerName, Reader reader) {
        Layer layer = null
        if (workspaceStr) {
            Workspace workspace = new Workspace(workspaceStr)
            if (layerName) {
                layer = workspace.get(layerName)
            } else {
                if (workspace.names.size() == 1) {
                    layer = workspace.get(workspace.names[0])
                } else if (workspaceStr.endsWith(".shp") || workspaceStr.endsWith(".properties")) {
                    String fileName = new File(workspaceStr).name
                    layer = workspace.get(fileName.substring(0, fileName.lastIndexOf(".")))
                }
            }
        } else {
            layer = new CsvReader().read(reader.text)
        }
        layer
    }
}
