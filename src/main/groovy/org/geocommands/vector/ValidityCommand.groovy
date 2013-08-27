package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import org.geocommands.vector.LayerCommand
import org.geocommands.vector.LayerOptions
import org.kohsuke.args4j.Option

/**
 * Check whether geometry in a Layer is valid or not
 * @author Jared Erickson
 */
class ValidityCommand extends LayerCommand<ValidityOptions>{

    private static String NEW_LINE = System.getProperty("line.separator")

    @Override
    String getName() {
        "vector validity"
    }

    @Override
    String getDescription() {
        "Check whether geometry in a Layer is valid or not"
    }

    @Override
    ValidityOptions getOptions() {
        new ValidityOptions()
    }

    @Override
    protected void processLayer(Layer layer, ValidityOptions options, Reader reader, Writer writer) throws Exception {
        layer.eachFeature {Feature f ->
            if (!f.geom.valid) {
                String reason = f.geom.validReason
                writer.write("${options.fields ? options.fields.collect{f.get(it)}.join(",") : f.id},${reason}${NEW_LINE}")
            }
        }
    }

    static class ValidityOptions extends LayerOptions {
        @Option(name="-f", aliases="--field",  usage="A Field to include when reporting an invalid Geometry", required = false)
        List<String> fields
    }
}
