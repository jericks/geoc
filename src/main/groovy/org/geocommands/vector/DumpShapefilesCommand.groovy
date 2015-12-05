package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.kohsuke.args4j.Option

/**
 * Create shapefiles from the input Layer
 * @author Jared Erickson
 */
class DumpShapefilesCommand extends LayerCommand<DumpShapefilesOptions>{

    @Override
    String getName() {
        "vector dump shapefiles"
    }

    @Override
    String getDescription() {
        "Create shapefiles from the input Layer"
    }

    @Override
    DumpShapefilesOptions getOptions() {
        new DumpShapefilesOptions()
    }

    @Override
    protected void processLayer(Layer layer, DumpShapefilesOptions options, Reader reader, Writer writer) throws Exception {
        if (!options.outputDirectory.exists()) {
            options.outputDirectory.mkdirs()
        }
        Shapefile.dump([maxShapeSize: options.maxShapeSize, maxDbfSize: options.maxDbfSize], options.outputDirectory, layer)
    }

    static class DumpShapefilesOptions extends LayerOptions {

        @Option(name = "-o", aliases = "--output-directory", usage = "The output directory", required = true)
        File outputDirectory

        @Option(name = "-s", aliases = "--max-shp-size", usage = "The maximum shp size", required = false)
        Long maxShapeSize

        @Option(name = "-d", aliases = "--max-dbf-size", usage = "The maximum dbf size", required = false)
        Long maxDbfSize

    }
}
