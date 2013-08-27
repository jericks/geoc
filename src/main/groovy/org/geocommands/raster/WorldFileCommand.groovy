package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.WorldFile
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Create a Raster world file
 * @author Jared Erickson
 */
class WorldFileCommand extends Command<WorldFileOptions> {

    @Override
    String getName() {
        "raster worldfile"
    }

    @Override
    String getDescription() {
        "Create a Raster world file"
    }

    @Override
    WorldFileOptions getOptions() {
        new WorldFileOptions()
    }

    @Override
    void execute(WorldFileOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Bounds.fromString(options.bounds)
        List size = options.size.split(",").collect{ it as double}
        File file = options.file
        boolean shouldUseWriter = false
        if (!options.file) {
            shouldUseWriter = true
            file = File.createTempFile("worldFile", "txt")
        }
        WorldFile worldFile = new WorldFile(bounds, size, file)
        if (shouldUseWriter) {
            writer.write(file.text)
        }
    }

    static class WorldFileOptions extends Options {

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = true)
        String bounds

        @Option(name = "-s", aliases = "--size", usage = "The size", required = true)
        String size

        @Option(name = "-f", aliases = "--file", usage = "The world file", required = false)
        File file
    }
}
