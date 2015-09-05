package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer

/**
 * A LayerInOtherOutCommand that combines the Schemas of the input and other Layers.
 * @author Jared Erickson
 */
abstract class LayerInOtherOutCombineSchemasCommand<T extends LayerInOtherOutCombineSchemasOptions> extends LayerInOtherOutCommand<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract void processLayers(Layer inLayer, Layer otherLayer, Layer outLayer, T options, Reader reader, Writer writer) throws Exception

    protected Schema createOutputSchema(Layer inputLayer, Layer otherLayer, T options) {
        Map schemaAndFields = inputLayer.schema.addSchema(otherLayer.schema, getOutputLayerName(inputLayer, otherLayer, name, options),
                postfixAll: options.postfixAll,
                maxFieldNameLength: Util.isWorkspaceStringShapefile(options.outputWorkspace) ? 10 : options.maxFieldNameLength,
                includeDuplicates: options.includeDuplicates,
                firstPostfix: options.firstPostfix,
                secondPostfix: options.secondPostfix
        )
        options.fields = schemaAndFields.fields
        schemaAndFields.schema
    }
}
