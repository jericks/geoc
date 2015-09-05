package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.Command

/**
 * A Command base class that processes two input Layers and creates an output Layer.
 * @author Jared Erickson
 */
abstract class LayerInOtherOutCommand<T extends LayerInOtherOutOptions> extends Command<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    void execute(T options, Reader reader, Writer writer) throws Exception {
        Layer inLayer = getInputLayer(options.inputWorkspace, options.inputLayer, reader)
        Layer otherLayer = Util.getOtherLayer(options.otherWorkspace, options.otherLayer)
        Layer outLayer = getOutputLayer(inLayer, otherLayer, options)
        try {
            processLayers(inLayer, otherLayer, outLayer, options, reader, writer)
            if (outLayer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
        }
        finally {
            inLayer.workspace.close()
            otherLayer.workspace.close()
            outLayer.workspace.close()
        }
    }

    abstract void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, T options, Reader reader, Writer writer) throws Exception

    Layer getInputLayer(String workspaceStr, String layerName, Reader reader) {
        Layer layer = null
        if (workspaceStr) {
            Workspace workspace = Workspace.getWorkspace(workspaceStr)
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

    Layer getOutputLayer(Layer inputLayer, Layer otherLayer, T options) {
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(options.outputWorkspace)
        }
        workspace.create(Util.checkSchema(createOutputSchema(inputLayer, otherLayer, options), inputLayer, options.outputWorkspace))
    }

    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, T options) {
        inputLayer.schema.addSchema(otherLayer.schema, getOutputLayerName(inputLayer, otherLayer, name, options),
                postfixAll: true, maxFieldNameLength: Util.isWorkspaceStringShapefile(options.outputWorkspace) ? 10 : -1).schema
    }

    protected String getOutputLayerName(Layer inputLayer, Layer otherLayer, String postfix, T options) {
        String outName = options.outputLayer ? options.outputLayer : "${inputLayer.name}_${otherLayer.name}_${postfix}"
        if (options.outputWorkspace &&
                (options.outputWorkspace.endsWith(".shp") || options.outputWorkspace.endsWith(".properties"))) {
            String fileName = new File(options.outputWorkspace).name
            outName = fileName.substring(0, fileName.lastIndexOf("."))
        }
        outName
    }

}

