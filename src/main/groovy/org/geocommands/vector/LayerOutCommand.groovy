package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.workspace.Memory
import org.geocommands.Command

/**
 * A Command base class for writing Layers.
 * @author Jared Erickson
 */
abstract class LayerOutCommand<T extends LayerOutOptions> extends Command<T> {

    abstract String getName()
    abstract String getDescription()
    abstract T getOptions()

    abstract Layer createLayer(T options, Reader reader, Writer writer) throws Exception

    void execute(T options, Reader reader, Writer writer) throws Exception {
        Layer layer = createLayer(options, reader, writer)
        try {
            if (layer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(layer))
            }
        }
        finally {
            layer.workspace.close()
        }
    }

    protected String getOutputLayerName(T options, String defaultName) {
        String outName = options.outputLayer ? options.outputLayer : defaultName
        if (options.outputWorkspace && (options.outputWorkspace.endsWith(".shp") || options.outputWorkspace.endsWith(".properties"))) {
            String fileName = new File(options.outputWorkspace).name
            outName = fileName.substring(0, fileName.lastIndexOf(".") )
        }
        outName
    }
}
