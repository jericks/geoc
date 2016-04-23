package org.geocommands.style

import geoscript.style.Style
import geoscript.style.io.SLDReader
import geoscript.style.io.YSLDWriter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert SLD to YSLD
 * @author Jared Erickson
 */
class SldToYsldCommand extends Command<SldToYsldOptions> {

    @Override
    String getName() {
        "style sld2ysld"
    }

    @Override
    String getDescription() {
        "Convert SLD to YSLD"
    }

    @Override
    SldToYsldOptions getOptions() {
        new SldToYsldOptions()
    }

    @Override
    void execute(SldToYsldOptions options, Reader reader, Writer writer) throws Exception {
        Style style
        SLDReader styleReader = new SLDReader()
        if (options.input) {
            if (options.input.startsWith("http")) {
                style = styleReader.read(new URL(options.input).text)
            } else {
                style = styleReader.read(new File(options.input))
            }
        } else {
            style = styleReader.read(reader.text)
        }
        if (style) {
            String sld = new YSLDWriter().write(style)
            if (options.output) {
                new File(options.output).write(sld)
            } else {
                writer.write(sld)
            }
        }
    }

    static class SldToYsldOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input file or url", required = false)
        String input

        @Option(name = "-o", aliases = "--output", usage = "The output file", required = false)
        String output

    }
}
