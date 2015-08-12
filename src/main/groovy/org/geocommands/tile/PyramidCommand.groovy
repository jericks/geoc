package org.geocommands.tile

import geoscript.layer.TileLayer
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get a Pyramid from a TileLayer.
 * @author Jared Erickson
 */
class PyramidCommand extends Command<PyramidOptions> {

    @Override
    String getName() {
        "tile pyramid"
    }

    @Override
    String getDescription() {
        "Get a Pyramid from a TileLayer"
    }

    @Override
    PyramidOptions getOptions() {
        new PyramidOptions()
    }

    @Override
    void execute(PyramidOptions options, Reader reader, Writer writer) throws Exception {
        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        if (options.outputType.equalsIgnoreCase("text") || options.outputType.equalsIgnoreCase("csv")) {
            writer.write(tileLayer.pyramid.getCsv())
        } else if (options.outputType.equalsIgnoreCase("json")) {
            writer.write(tileLayer.pyramid.getJson())
        } else if (options.outputType.equalsIgnoreCase("xml")) {
            writer.write(tileLayer.pyramid.getXml())
        }
    }

    static class PyramidOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-o", aliases = "--output-type", usage = "The output type (text, xml, json)", required = false)
        String outputType = "text"

    }
}
