package org.geocommands.vector

import geoscript.geom.Geometry
import org.kohsuke.args4j.Option

/**
 * Densify the features of the input Layer and save them to the output Layer
 * @author Jared Erickson
 */
class DensifyCommand extends TransformGeometryCommand<DensifyOptions> {

    @Override
    String getName() {
        "vector densify"
    }

    @Override
    String getDescription() {
        "Densify the features of the input Layer and save them to the output Layer"
    }

    @Override
    DensifyOptions getOptions() {
        new DensifyOptions()
    }

    @Override
    Geometry transformGeometry(Geometry geometry, DensifyOptions options) {
        geometry.densify(options.distanceTolerance)
    }

    static class DensifyOptions extends LayerInOutOptions {
        @Option(name="-d", aliases="--distance",  usage="The distance tolerance", required = true)
        double distanceTolerance
    }
}


