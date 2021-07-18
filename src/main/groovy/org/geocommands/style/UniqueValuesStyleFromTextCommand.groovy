package org.geocommands.style

import geoscript.style.Style
import geoscript.style.io.SLDWriter
import geoscript.style.io.UniqueValuesReader
import geoscript.style.io.YSLDWriter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class UniqueValuesStyleFromTextCommand extends Command<UniqueValuesStyleFromTextOptions> {

    @Override
    String getName() {
        "style uniquevaluesfromtext"
    }

    @Override
    String getDescription() {
        "Create a Style from reading values in the unique values format"
    }

    @Override
    UniqueValuesStyleFromTextOptions getOptions() {
        new UniqueValuesStyleFromTextOptions()
    }

    @Override
    void execute(UniqueValuesStyleFromTextOptions options, Reader reader, Writer writer) throws Exception {
        // Read
        Style style
        UniqueValuesReader uniqueValuesReader = new UniqueValuesReader(options.field, options.geometryType)
        if (options.input) {
            if (options.input.startsWith("http")) {
                style = uniqueValuesReader.read(new URL(options.input).text)
            } else {
                style = uniqueValuesReader.read(new File(options.input))
            }
        } else {
            style = uniqueValuesReader.read(reader.text)
        }
        // Write
        String styleStr = ""
        if (options.type.equalsIgnoreCase("ysld")) {
            YSLDWriter styleWriter = new YSLDWriter()
            styleStr = styleWriter.write(style)
        } else {
            SLDWriter styleWriter = new SLDWriter()
            styleStr = styleWriter.write(options.writerOptions, style)
        }
        if (options.output) {
            new File(options.output).write(styleStr)
        } else {
            writer.write(styleStr)
        }
    }

    static class UniqueValuesStyleFromTextOptions extends Options {

        @Option(name = "-f", aliases = "--field", usage = "The field", required = true)
        String field

        @Option(name = "-g", aliases = "--geometry-type", usage = "The geometry type (point, linestring, polygon)", required = true)
        String geometryType

        @Option(name = "-i", aliases = "--input", usage = "The input file or url", required = false)
        String input

        @Option(name = "-t", aliases = "--type", usage = "The output type (sld or ysld)", required = false)
        String type = "sld"

        @Option(name = "-o", aliases = "--output", usage = "The output file", required = false)
        String output

        @Option(name = "-w", aliases = "--writer-options", usage = "The StyleWriter options", required = false)
        Map<String,String> writerOptions = [:]

    }

}
