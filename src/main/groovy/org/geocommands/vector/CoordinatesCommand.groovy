package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.geom.Point
import geoscript.layer.Layer

/**
 * Extract all of the Coordinates from the input Layer and save them to
 * the output Layer.
 * @author Jared Erickson
 */
class CoordinatesCommand extends LayerInOutCommand<CoordinatesOptions> {

    @Override
    String getName() {
        "vector coordinates"
    }

    @Override
    String getDescription() {
        "Extract coordinates from the input Layer and save them to the output Layer."
    }

    @Override
    CoordinatesOptions getOptions() {
        new CoordinatesOptions()
    }

    @Override
    protected Schema createOutputSchema(Layer layer, CoordinatesOptions options) {
        layer.schema.changeGeometryType("Point", getOutputLayerName(layer, "coordinates", options))
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, CoordinatesOptions options, Reader reader, Writer writer) {
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                f.geom.coordinates.each { coord ->
                    Map values = [:]
                    f.attributes.each { k, v ->
                        if (v instanceof geoscript.geom.Geometry) {
                            values[k] = new Point(coord.x, coord.y)
                        } else {
                            values[k] = v
                        }
                    }
                    w.add(outLayer.schema.feature(values))
                }
            }
        }
    }

    static class CoordinatesOptions extends LayerInOutOptions {
    }
}
