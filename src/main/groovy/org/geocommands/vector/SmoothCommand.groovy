package org.geocommands.vector

import geoscript.geom.Geometry
import org.kohsuke.args4j.Option

/**
 * Smooth the features of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class SmoothCommand extends TransformGeometryCommand<SmoothOptions> {

    @Override
    String getName() {
        "vector smooth"
    }

    @Override
    String getDescription() {
        "Smooth the features of the input Layer and save them to the output Layer"
    }

    @Override
    SmoothOptions getOptions() {
        new SmoothOptions()
    }

    @Override
    Geometry transformGeometry(Geometry geometry, SmoothOptions options) {
        geometry.smooth(options.fit)
    }

    static class SmoothOptions extends LayerInOutOptions {
        @Option(name = "-f", aliases = "--fit", usage = "The amount of smoothing (between 0 - more and 1 - less)", required = true)
        double fit
    }
}


