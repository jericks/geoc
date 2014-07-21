package org.geocommands.style

import geoscript.style.Style
import geoscript.style.io.CSSReader
import geoscript.style.io.SLDWriter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert CSS to SLD
 * @author Jared Erickson
 */
class CssToSldCommand extends Command<CssToSldOptions> {

    @Override
    String getName() {
        "css2sld"
    }

    @Override
    String getDescription() {
        "Convert CSS to SLD"
    }

    @Override
    CssToSldOptions getOptions() {
        new CssToSldOptions()
    }

    @Override
    void execute(CssToSldOptions options, Reader reader, Writer writer) throws Exception {
        Style style
        CSSReader cssReader = new CSSReader()
        if (options.input) {
            if (options.input.startsWith("http")) {
                style = cssReader.read(new URL(options.input).text)
            } else {
                style = cssReader.read(new File(options.input))
            }
        } else {
            style = cssReader.read(reader)
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

    static class CssToSldOptions extends Options {

        @Option(name="-i", aliases="--input",  usage="The input file or url", required = false)
        String input

        @Option(name="-o", aliases="--output",  usage="The output file", required = false)
        String output

    }
}
