package org.geocommands.raster

import geoscript.layer.Layer
import geoscript.layer.Raster

/**
 * Get the Envelope of a Raster as a Vector Layer.
 * @author Jared Erickson
 */
class EnvelopeCommand extends RasterToVectorCommand<EnvelopeOptions> {

    @Override
    String getName() {
        "raster envelope"
    }

    @Override
    String getDescription() {
        "Get the Envelope of a Raster as a Vector Layer"
    }

    @Override
    EnvelopeOptions getOptions() {
        new EnvelopeOptions()
    }

    @Override
    void convertRasterToVector(Raster raster, Layer layer, EnvelopeOptions options, Reader reader, Writer writer) throws Exception {
        layer.add([raster.bounds.geometry])
    }

    static class EnvelopeOptions extends RasterToVectorOptions {
    }
}
