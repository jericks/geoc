package org.geocommands

import geoscript.proj.Projection
import org.kohsuke.args4j.Option

/**
 * Create a prj file
 * @author Jared Erickson
 */
class PrjCommand extends Command<PrjOptions> {

    @Override
    String getName() {
        "prj"
    }

    @Override
    String getDescription() {
        "Create a prj file"
    }

    @Override
    PrjOptions getOptions() {
        new PrjOptions()
    }

    @Override
    void execute(PrjOptions options, Reader reader, Writer writer) throws Exception {
        Projection proj = new Projection(options.epsg)
        if (options.file) {
            options.file.text = proj.wkt
        } else {
            writer.write(proj.wkt)
        }
    }

    static class PrjOptions extends Options {

        @Option(name = "-e", aliases = "--epsg", usage = "The EPSG Projection code", required = true)
        String epsg

        @Option(name = "-f", aliases = "--file", usage = "The output File", required = false)
        File file

    }
}
