package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.geom.Geometry
import geoscript.layer.Layer

/**
 * An abstract LayerInOutCommand that simply transforms the Geometry.
 * @author Jared Erickson
 */
abstract class TransformGeometryCommand<T extends LayerInOutOptions> extends LayerInOutCommand<T> {

    abstract String getName()

    abstract String getDescription()

    abstract T getOptions()

    abstract Geometry transformGeometry(Geometry geometry, T options)

    @Override
    void processLayers(Layer inLayer, Layer outLayer, T options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Map values = [:]
                f.attributes.each { k, v ->
                    if (v instanceof geoscript.geom.Geometry) {
                        values[k] = transformGeometry(v, options)
                    } else {
                        values[k] = v
                    }
                }
                w.add(outLayer.schema.feature(values, f.id))
            }
        }
    }
}
