package org.geocommands.vector

import geoscript.filter.Color
import geoscript.layer.Layer
import geoscript.style.UniqueValues
import org.geocommands.vector.LayerCommand
import org.geocommands.vector.LayerOptions
import org.kohsuke.args4j.Option

/**
 * Create an SLD document where each unique value in the Layer is a rule.
 * @author Jared Erickson
 */
class UniqueValuesStyleCommand extends LayerCommand<UniqueValuesStyleOptions> {

    @Override
    String getName() {
        "vector uniquevaluesstyle"
    }

    @Override
    String getDescription() {
        "Create an SLD document where each unique value in the Layer is a rule."
    }

    @Override
    UniqueValuesStyleOptions getOptions() {
        new UniqueValuesStyleOptions()
    }

    @Override
    protected void processLayer(Layer layer, UniqueValuesStyleOptions options, Reader reader, Writer writer) throws Exception {
        def colors = {index, value -> Color.getRandomPastel()}
        if (options.colors) {
            def colorList = options.colors.split(" ")
            if (colorList.length > 1) {
                colors = []
                colors.addAll(colorList)
            } else {
                colors = options.colors as String
            }
        }
        UniqueValues uniqueValues = new UniqueValues(layer,options.field, colors)
        writer.write(uniqueValues.sld.trim())
    }

    static class UniqueValuesStyleOptions extends LayerOptions {

        @Option(name="-f", aliases="--field",  usage="The field name", required = true)
        String field

        @Option(name="-c", aliases="--colors",  usage="The color brewer palette name or a list of colors (space delimited)", required = false)
        String colors

    }
}
