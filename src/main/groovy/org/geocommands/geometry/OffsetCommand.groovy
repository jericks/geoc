package org.geocommands.geometry

import geoscript.geom.Geometry
import org.geocommands.Command
import org.geocommands.Options
import org.geotools.geometry.jts.OffsetCurveBuilder
import org.kohsuke.args4j.Option

/**
 * Create a Geometry offset from the input Geometry.
 * @author Jared Erickson
 */
class OffsetCommand extends Command<OffsetOptions> {

    @Override
    String getName() {
        "geometry offset"
    }

    @Override
    String getDescription() {
        "Create a Geometry offset from the input Geometry"
    }

    @Override
    OffsetOptions getOptions() {
        new OffsetOptions()
    }

    @Override
    void execute(OffsetOptions options, Reader reader, Writer writer) throws Exception {
        Geometry geom = Geometry.fromString(options.input ? options.input : reader.text)
        Geometry offsetGeom = geom.offset(options.offset, options.quadrantSegments)
        writer.write(offsetGeom.wkt)
    }

    static class OffsetOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geometry", required = false)
        String input

        @Option(name = "-d", aliases = "--offset", usage = "The offset distance", required = true)
        double offset

        @Option(name = "-s", aliases = "--quadrant-segements", usage = "The number of quadrant segments (defaults to 8)", required = false)
        int quadrantSegments = 8

    }
}
