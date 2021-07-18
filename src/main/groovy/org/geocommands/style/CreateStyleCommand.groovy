package org.geocommands.style

import geoscript.style.Style
import geoscript.style.io.SLDWriter
import geoscript.style.io.Writer as StyleWriter
import geoscript.style.io.SimpleStyleReader
import geoscript.style.io.YSLDWriter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Create a simple style
 * @author Jared Erickson
 */
class CreateStyleCommand extends Command<CreateStyleOptions> {

    @Override
    String getName() {
        "style create"
    }

    @Override
    String getDescription() {
        "Create a simple style"
    }

    @Override
    CreateStyleOptions getOptions() {
        new CreateStyleOptions()
    }

    @Override
    void execute(CreateStyleOptions options, Reader reader, Writer writer) throws Exception {
        Style style = new SimpleStyleReader().read(options.options)
        String styleStr = ""
        if (options.type.equalsIgnoreCase("ysld")) {
            StyleWriter styleWriter = new YSLDWriter()
            styleStr = styleWriter.write(style)
        } else {
            StyleWriter styleWriter = new SLDWriter()
            styleStr = styleWriter.write(options.writerOptions, style)
        }
        if (options.output) {
            new File(options.output).write(styleStr)
        } else {
            writer.write(styleStr)
        }
    }

    static class CreateStyleOptions extends Options {

        @Option(name = "-s", aliases = "--style-options", usage = "A style options", required = true)
        Map<String,String> options

        @Option(name = "-t", aliases = "--type", usage = "The output type (sld or ysld)", required = false)
        String type = "sld"

        @Option(name = "-w", aliases = "--writer-options", usage = "The StyleWriter options", required = false)
        Map<String,String> writerOptions = [:]

        @Option(name = "-o", aliases = "--output", usage = "The output file", required = false)
        String output

    }
}
