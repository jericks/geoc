package org.geocommands.carto

import geoscript.carto.CartoBuilder
import geoscript.carto.io.CartoReader
import geoscript.carto.io.CartoReaders
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class MapCommand extends Command<MapOptions>{

    @Override
    String getName() {
        "carto map"
    }

    @Override
    String getDescription() {
        "Create a cartographic map"
    }

    @Override
    MapOptions getOptions() {
        new MapOptions()
    }

    @Override
    void execute(MapOptions options, Reader reader, Writer writer) throws Exception {
        CartoReader cartoReader = CartoReaders.find(options.type)
        CartoBuilder cartoBuilder = cartoReader.read(options.cartoFile ? options.cartoFile.text : reader.text)
        options.outputFile.withOutputStream { OutputStream outputStream ->
            cartoBuilder.build(outputStream)
        }
    }

    static class MapOptions extends Options {

        @Option(name = "-t", aliases = "--type", usage = "The type of the carto file (json, xml)", required = true)
        String type

        @Option(name = "-c", aliases = "--carto-file", usage = "The input carto definition file", required = false)
        File cartoFile

        @Option(name = "-o", aliases = "--output-file", usage = "The output file", required = true)
        File outputFile

    }

}
