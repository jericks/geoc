package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Compare the schema of two Layers.
 * @author Jared Erickson
 */
class CompareSchemasCommand extends LayerCommand<CompareSchemasOptions> {

    @Override
    String getName() {
        "vector compareschemas"
    }

    @Override
    String getDescription() {
        "Compare Schemas from two Layers"
    }

    @Override
    CompareSchemasOptions getOptions() {
        new CompareSchemasOptions()
    }

    @Override
    protected void processLayer(Layer layer, CompareSchemasOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        Layer otherLayer = Util.getOtherLayer(options.otherWorkspace, options.otherLayer)
        try {
            List fields = []
            int maxNameLength = Integer.MIN_VALUE
            int maxTypeLength = Integer.MIN_VALUE
            layer.schema.fields.each {fld ->
                Map field = [name: fld.name, typ: fld.typ, present1: true, present2: otherLayer.schema.has(fld) && otherLayer.schema[fld.name].typ.equals(fld.typ)]
                fields.add(field)
                maxNameLength = Math.max(fld.name.length(), maxNameLength)
                maxTypeLength = Math.max(fld.typ.length(), maxTypeLength)
            }

            otherLayer.schema.fields.each {fld ->
                if (!fields.find{f -> f.name.equals(fld.name) && f.typ.equals(fld.typ)}) {
                    Map field = [name: fld.name, typ: fld.typ, present1: false, present2: true]
                    fields.add(field)
                    maxNameLength = Math.max(fld.name.length(), maxNameLength)
                    maxTypeLength = Math.max(fld.typ.length(), maxTypeLength)
                }
            }

            if (options.prettyPrint) {
                int layer1NameLength = Math.max(5, layer.name.length())
                int layer2NameLength = Math.max(5, otherLayer.name.length())
                String header = "| ${'Name'.padRight(maxNameLength)} | ${'Type'.padRight(maxTypeLength)} | ${layer.name.padRight(layer1NameLength)} | ${otherLayer.name.padRight(layer2NameLength)} |"
                String line = "-".padLeft(header.length(),"-")
                writer.write(line)
                writer.write(NEW_LINE)
                writer.write(header)
                writer.write(NEW_LINE)
                writer.write(line)
                writer.write(NEW_LINE)
                fields.each{fld ->
                    writer.write("| ${fld.name.padRight(maxNameLength)} | ${fld.typ.padRight(maxTypeLength)} | ${String.valueOf(fld.present1).padRight(layer1NameLength)} | ${String.valueOf(fld.present2).padRight(layer2NameLength)} |")
                    writer.write(NEW_LINE)
                }
                writer.write(line)
            } else {
                writer.write("Name,Type,${layer.name},${otherLayer.name}")
                writer.write(NEW_LINE)
                fields.eachWithIndex{fld,i ->
                    if (i > 0) writer.write(NEW_LINE)
                    writer.write("${fld.name},${fld.typ},${fld.present1},${fld.present2}")
                }
            }
        }
        finally {
            otherLayer.workspace.close()
        }
    }

    static class CompareSchemasOptions extends LayerOptions{

        @Option(name="-k", aliases="--other-workspace",  usage="The other workspace", required = true)
        String otherWorkspace

        @Option(name="-y", aliases="--other-layer",  usage="The other layer", required = false)
        String otherLayer

        @Option(name="-p", aliases="--pretty-print", usage="Whether to pretty print the output", required=false)
        boolean prettyPrint = false
    }
}
