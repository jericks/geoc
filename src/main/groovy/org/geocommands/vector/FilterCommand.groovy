package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import org.kohsuke.args4j.Option

/**
 * Filter features from the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class FilterCommand extends LayerInOutCommand<FilterOptions> {

    @Override
    String getName() {
        "vector filter"
    }

    @Override
    String getDescription() {
        "Filter features from the input Layer and save them to the output Layer"
    }

    @Override
    FilterOptions getOptions() {
        new FilterOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, FilterOptions options, Reader reader, Writer writer) {
        outLayer.withWriter {geoscript.layer.Writer w ->
            inLayer.eachFeature(options.filter, {Feature f ->
                Map values = [:]
                f.attributes.each{k,v ->
                    values[k] = v
                }
                w.add(outLayer.schema.feature(values, f.id))
            })
        }
    }

    static class FilterOptions extends LayerInOutOptions {
        @Option(name="-f", aliases="--filter",  usage="The CQL Filter", required = true)
        String filter
    }
}