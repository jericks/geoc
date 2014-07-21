package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.MapAlgebra
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Perform map algebra.
 * @author Jared Erickson
 */
class MapAlgebraCommand extends Command<MapAlgebraOptions> {

    @Override
    String getName() {
        "raster mapalgebra"
    }

    @Override
    String getDescription() {
        "Perform map algebra"
    }

    @Override
    MapAlgebraOptions getOptions() {
        new MapAlgebraOptions()
    }

    @Override
    void execute(MapAlgebraOptions options, Reader reader, Writer writer) throws Exception {
        String script = options.script
        if (!options.script) {
            script = reader.text
        }
        Map<String, Raster> rasters = [:]
        options.rasters.each { k, v ->
            String format = v
            String name = ""
            if (format.contains(";")) {
                name = format.substring(format.lastIndexOf(";"))
            }
            rasters[k] = RasterUtil.getRaster(v, name, null)
        }
        MapAlgebra mapAlgebra = new MapAlgebra()
        Raster raster = mapAlgebra.calculate(script, rasters,
                outputName: options.outputName,
                size: options.size?.split(","),
                bounds: Bounds.fromString(options.bounds)
        )
        if (options.outputProjection) {
            Projection p = new Projection(options.outputProjection)
            if (!p.equals(raster.proj)) {
                raster = raster.reproject(p)
            }
        }
        RasterUtil.writeRaster(raster, options.outputFormat, options.outputRaster, writer)
    }

    static class MapAlgebraOptions extends Options {

        @Option(name = "-s", aliases = "--script", usage = "The map algebra (jiffle) script", required = false)
        String script

        @Option(name = "-r", aliases = "--raster", usage = "An input Raster", required = false)
        Map<String, String> rasters = [:]

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-z", aliases = "--size", usage = "The size", required = false)
        String size

        @Option(name = "-n", aliases = "--output-name", usage = "The output name", required = false)
        String outputName = "dest"

        @Option(name = "-o", aliases = "--output-raster", usage = "The output raster", required = false)
        String outputRaster

        @Option(name = "-f", aliases = "--output-raster-format", usage = "The output raster format", required = false)
        String outputFormat

        @Option(name = "-p", aliases = "--output-raster-projection", usage = "The output raster projection", required = false)
        String outputProjection
    }
}
