package org.geocommands.style

import geoscript.style.Style
import geoscript.style.io.SLDWriter
import geoscript.style.io.YSLDReader
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert YSLD to SLD
 * @author Jared Erickson
 */
class YsldToSldCommand extends Command<YsldToSldOptions> {

    @Override
    String getName() {
        "style ysld2sld"
    }

    @Override
    String getDescription() {
        "Convert YSLD to SLD"
    }

    @Override
    YsldToSldOptions getOptions() {
        new YsldToSldOptions()
    }

    @Override
    void execute(YsldToSldOptions options, Reader reader, Writer writer) throws Exception {
        Style style
        YSLDReader styleReader = new YSLDReader()
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
            String sld = new SLDWriter().write(style)
            if (options.output) {
                new File(options.output).write(sld)
            } else {
                writer.write(sld)
            }
        }
    }

    static class YsldToSldOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input file or url", required = false)
        String input

        @Option(name = "-o", aliases = "--output", usage = "The output file", required = false)
        String output

    }
}
