package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.Raster
import org.kohsuke.args4j.Option

/**
 * Get the cell value from a Raster
 * @author Jared Erickson
 */
class GetValueCommand extends RasterCommand<GetValueOptions> {

    @Override
    String getName() {
        "raster get value"
    }

    @Override
    String getDescription() {
        "Get the cell value from a Raster"
    }

    @Override
    GetValueOptions getOptions() {
        new GetValueOptions()
    }

    @Override
    protected void processRaster(Raster raster, GetValueOptions options, Reader reader, Writer writer) throws Exception {
        List values
        if (options.type.equalsIgnoreCase("point")) {
            values = raster.eval(new Point(options.x, options.y))
        } else {
            values = raster.eval(options.x as int, options.y as int)
        }
        if (options.band > -1) {
            values = [values[options.band - 1]]
        }
        String NEW_LINE = System.getProperty("line.separator")
        values.each { value ->
            writer.write("${value}")
            writer.write(NEW_LINE)
        }
    }

    static class GetValueOptions extends RasterOptions {

        @Option(name = "-x", aliases = "--x-coordinate", usage = "The x coordinate", required = true)
        double x

        @Option(name = "-y", aliases = "--y-coordinate", usage = "The y coordinate", required = true)
        double y

        @Option(name = "-t", aliases = "--type", usage = "The type can be point or pixel", required = false)
        String type = "point"

        @Option(name = "-b", aliases = "--band", usage = "The band to get a value from", required = false)
        int band = -1

    }
}
