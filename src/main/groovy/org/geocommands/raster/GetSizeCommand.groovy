package org.geocommands.raster

import geoscript.layer.Raster

class GetSizeCommand extends RasterCommand<GetSizeOptions> {

    @Override
    String getName() {
        "raster size"
    }

    @Override
    String getDescription() {
        "Get the Raster size (width,height)"
    }

    @Override
    GetSizeOptions getOptions() {
        new GetSizeOptions()
    }

    @Override
    protected void processRaster(Raster raster, GetSizeOptions options, Reader reader, Writer writer) throws Exception {
        List size = raster.size
        writer.write("${size[0]},${size[1]}")
    }

    static class GetSizeOptions extends RasterOptions {
    }
}
