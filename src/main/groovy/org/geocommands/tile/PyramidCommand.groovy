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
        TileLayer tileLayer = TileUtil.getTileLayer(options.tileLayer, options.tileLayerName, options.type, options.pyramid)
        writer.write(PyramidUtil.writePyramid(tileLayer.pyramid, options.outputType))
    }

    static class PyramidOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-n", aliases = "--tile-layer-name", usage = "The tile layer name", required = true)
        String tileLayerName

        @Option(name = "-t", aliases = "--type", usage = "The type of tile layer(png, utfgrid, mvt, pbf)", required = false)
        String type = "png"

        @Option(name = "-p", aliases = "--pyramid", usage = "The pyramid", required = false)
        String pyramid = "GlobalMercator"

        @Option(name = "-o", aliases = "--output-type", usage = "The output type (text, xml, json)", required = false)
        String outputType = "text"

    }
}
