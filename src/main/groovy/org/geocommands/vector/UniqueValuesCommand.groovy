package org.geocommands.vector

import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * List the unique values in a Layer's Field
 * @author Jared Erickson
 */
class UniqueValuesCommand extends LayerCommand<UniqueValuesOptions> {

    @Override
    String getName() {
        "vector uniquevalues"
    }

    @Override
    String getDescription() {
        "List the unique values in a Layer's Field"
    }

    @Override
    UniqueValuesOptions getOptions() {
        new UniqueValuesOptions()
    }

    @Override
    protected void processLayer(Layer layer, UniqueValuesOptions options, Reader reader, Writer writer) throws Exception {
        // Collect
        Set values = []
        layer.eachFeature {f ->
            values.add(f[options.field])
        }
        // Sort
        List sortedValues = values.sort()
        // Display
        String NEW_LINE = System.getProperty("line.separator")
        sortedValues.eachWithIndex{v,i ->
            if (i > 0) writer.append(NEW_LINE)
            writer.append(String.valueOf(v))
        }
    }

    static class UniqueValuesOptions extends LayerOptions {

        @Option(name="-f", aliases="--field",  usage="The field name", required = true)
        String field

    }
}
