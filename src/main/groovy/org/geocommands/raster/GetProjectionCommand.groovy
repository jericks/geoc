package org.geocommands.raster

import geoscript.layer.Raster
import geoscript.proj.Projection
import org.kohsuke.args4j.Option

class GetProjectionCommand extends RasterCommand<GetProjectionOptions> {

    @Override
    String getName() {
        "raster projection"
    }

    @Override
    String getDescription() {
        "Get the Raster Projection"
    }

    @Override
    GetProjectionOptions getOptions() {
        new GetProjectionOptions()
    }

    @Override
    protected void processRaster(Raster raster, GetProjectionOptions options, Reader reader, Writer writer) throws Exception {
        Projection proj = raster.proj
        String projStr
        if (options.type.equalsIgnoreCase("epsg")) {
            projStr = proj.epsg
        } else if (options.type.equalsIgnoreCase("wkt")) {
            projStr = proj.wkt
        } else if (options.type.equalsIgnoreCase("id")) {
            projStr = proj.id
        } else if (options.type.equalsIgnoreCase("srs")) {
            projStr = proj.srs
        }
        writer.write("${projStr}")
    }

    static class GetProjectionOptions extends RasterOptions {

        @Option(name = "-t", aliases = "--type", usage = "The output type (epsg, id, srs, wkt)", required = false)
        String type = "id"

    }
  
}