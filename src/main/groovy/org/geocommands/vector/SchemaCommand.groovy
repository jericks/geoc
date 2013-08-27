package org.geocommands.vector

import geoscript.layer.Layer
import org.geocommands.vector.LayerCommand
import org.geocommands.vector.LayerOptions
import org.kohsuke.args4j.Option

/**
 * Get the Layer's Schema
 * @author Jared Erickson
 */
class SchemaCommand extends LayerCommand<SchemaOptions> {

    @Override
    String getName() {
        "vector schema"
    }

    @Override
    String getDescription() {
        "Get the Layer's schema"
    }

    @Override
    SchemaOptions getOptions() {
        new SchemaOptions()
    }

    @Override
    protected void processLayer(Layer layer, SchemaOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        if (!options.prettyPrint) {
            writer.write("name,type")
            writer.write(NEW_LINE)
            layer.schema.fields.eachWithIndex{fld,i ->
                if (i > 0) writer.write(NEW_LINE)
                writer.write("${fld.name},${fld.typ}")
            }
        } else {
            int maxNameLength = Integer.MIN_VALUE
            int maxTypeLength = Integer.MIN_VALUE
            layer.schema.fields.each {fld ->
                maxNameLength = Math.max(fld.name.length(), maxNameLength)
                maxTypeLength = Math.max(fld.typ.length(), maxTypeLength)
            }
            String header = "| ${'name'.padRight(maxNameLength)} | ${'type'.padRight(maxTypeLength)} |"
            String line = "-".padLeft(header.length(),"-")
            writer.write(line)
            writer.write(NEW_LINE)
            writer.write(header)
            writer.write(NEW_LINE)
            writer.write(line)
            writer.write(NEW_LINE)
            layer.schema.fields.each {fld ->
                writer.write("| ${fld.name.padRight(maxNameLength)} | ${fld.typ.padRight(maxTypeLength)} |")
                writer.write(NEW_LINE)
            }
            writer.write(line)
        }

    }

    static class SchemaOptions extends LayerOptions {

        @Option(name="-p", aliases="--pretty-print", usage="Whether to pretty print the output", required=false)
        boolean prettyPrint = false

    }
}
