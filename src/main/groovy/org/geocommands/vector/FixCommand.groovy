package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.geom.Geometry
import geoscript.layer.Layer

/**
 * Fix the geometry of the features of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class FixCommand extends LayerInOutCommand<FixOptions> {

    @Override
    String getName() {
        "vector fix"
    }

    @Override
    String getDescription() {
        "Fix the geometry of the features of the input Layer and save them to the output Layer"
    }

    @Override
    FixOptions getOptions() {
        new FixOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, FixOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        Geometry geom = v as Geometry
                        values[k] = geom.fix()
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }

    static class FixOptions extends LayerInOutOptions {
    }
}
