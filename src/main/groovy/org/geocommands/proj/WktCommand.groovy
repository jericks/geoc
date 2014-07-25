package org.geocommands.proj

import geoscript.proj.Projection
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Create a prj file
 * @author Jared Erickson
 */
class WktCommand extends Command<WktOptions> {

    @Override
    String getName() {
        "proj wkt"
    }

    @Override
    String getDescription() {
        "Get the WKT of a Projection"
    }

    @Override
    WktOptions getOptions() {
        new WktOptions()
    }

    @Override
    void execute(WktOptions options, Reader reader, Writer writer) throws Exception {
        Projection proj = new Projection(options.epsg)
        if (options.file) {
            options.file.text = proj.wkt
        } else {
            writer.write(proj.wkt)
        }
    }

    static class WktOptions extends Options {

        @Option(name = "-e", aliases = "--epsg", usage = "The EPSG Projection code", required = true)
        String epsg

        @Option(name = "-f", aliases = "--file", usage = "The output File", required = false)
        File file

    }
}
