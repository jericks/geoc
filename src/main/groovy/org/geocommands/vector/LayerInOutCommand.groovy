package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace

/**
 * A base class for Vector Commands that read in a Layer and then write out a Layer.
 * @author Jared Erickson
 */
abstract class LayerInOutCommand<T extends LayerInOutOptions> extends LayerCommand<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract void processLayers(Layer inLayer, Layer outLayer, T options, Reader reader, Writer writer) throws Exception

    @Override
    protected void processLayer(Layer inLayer, T options, Reader reader, Writer writer) throws Exception {
        Layer outLayer = getOutputLayer(inLayer, options)
        try {
            processLayers(inLayer, outLayer, options, reader, writer)
            if (outLayer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
        }
        finally {
            outLayer.workspace.close()
        }
    }

    protected Schema createOutputSchema(Layer layer, T options) {
        new Schema(getOutputLayerName(layer, name, options), layer.schema.fields)
    }

    protected String getOutputLayerName(Layer layer, String postfix, T options) {
        String outName = options.outputLayer ? options.outputLayer : layer.name + "_" + postfix
        if (options.outputWorkspace &&
                (options.outputWorkspace.endsWith(".shp") || options.outputWorkspace.endsWith(".properties"))) {
            String fileName = new File(options.outputWorkspace).name
            outName = fileName.substring(0, fileName.lastIndexOf("."))
        }
        outName
    }

    Layer getOutputLayer(Layer inputLayer, T options) {
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = Workspace.getWorkspace(options.outputWorkspace)
        }
        workspace.create(Util.checkSchema(createOutputSchema(inputLayer, options), inputLayer, options.outputWorkspace))
    }

}
