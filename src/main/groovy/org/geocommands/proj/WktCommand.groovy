package org.geocommands.proj

import geoscript.proj.Projection
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get the WKT of a Projection.  This can be used to create a prj File.
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
        String wkt = proj.getWkt(options.citation, options.indentation)
        if (options.file) {
            options.file.text = wkt
        } else {
            writer.write(wkt)
        }
    }

    static class WktOptions extends Options {

        @Option(name = "-e", aliases = "--epsg", usage = "The EPSG Projection code", required = true)
        String epsg

        @Option(name = "-f", aliases = "--file", usage = "The output File", required = false)
        File file

        @Option(name = "-c", aliases = "--citation", usage = "The citations (epsg or esri)", required = false)
        String citation = "epsg"

        @Option(name = "-i", aliases = "--indentation", usage = "The number of spaces to indent", required = false)
        int indentation = 2

    }
}
