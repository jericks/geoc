package org.geocommands.raster

import geoscript.geom.Geometry
import geoscript.geom.GeometryCollection
import geoscript.layer.Layer
import geoscript.layer.Raster
import org.geocommands.vector.Util
import org.kohsuke.args4j.Option

/**
 * Crop a Raster using the geometry from a Layer
 * @author Jared Erickson
 */
class CropWithLayerCommand extends RasterInOutCommand<CropWithLayerOptions> {

    @Override
    String getName() {
        "raster crop with layer"
    }

    @Override
    String getDescription() {
        "Crop a Raster using the geometry from a Layer"
    }

    @Override
    CropWithLayerOptions getOptions() {
        new CropWithLayerOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, CropWithLayerOptions options, Reader reader, Writer writer) throws Exception {
        Layer layer = Util.getInputLayer(options.inputWorkspace, options.inputLayer, null)
        Geometry geometry = new GeometryCollection(layer.collectFromFeature { f -> f.geom })
        inRaster.crop(geometry)
    }

    static class CropWithLayerOptions extends RasterInOutOptions {

        @Option(name = "-w", aliases = "--input-workspace", usage = "The input workspace", required = true)
        String inputWorkspace

        @Option(name = "-y", aliases = "--input-layer", usage = "The input layer", required = false)
        String inputLayer

    }
}
