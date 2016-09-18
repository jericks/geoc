package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Pyramid
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get a list of tiles for a given geometry
 * @author Jared Erickson
 */
class ListTilesCommand extends Command<ListTilesOptions> {

    @Override
    String getName() {
        "tile list tiles"
    }

    @Override
    String getDescription() {
        "Get a list of tiles for a given geometry"
    }

    @Override
    ListTilesOptions getOptions() {
        new ListTilesOptions()
    }

    @Override
    void execute(ListTilesOptions options, Reader reader, Writer writer) throws Exception {
        String NEW_LINE = System.getProperty("line.separator")
        Pyramid pyramid = Pyramid.fromString(options.pyramid ?: reader.text)
        Bounds bounds = Bounds.fromString(options.bounds)
        if (!bounds.proj) {
            bounds.proj = pyramid.proj
        } else if (bounds.proj != pyramid.bounds) {
            bounds = bounds.reproject(pyramid.proj)
        }
        Map tileCoords = pyramid.getTileCoordinates(bounds, options.z)
        long minX = tileCoords.minX
        long minY = tileCoords.minY
        long maxX = tileCoords.maxX
        long maxY = tileCoords.maxY
        (minY..maxY).each { long y ->
            (minX..maxX).each { long x ->
                writer.write("${options.z}/${x}/${y}" + NEW_LINE)
            }
        }
    }

    static class ListTilesOptions extends Options {

        @Option(name = "-p", aliases = "--pyramid", usage = "The tile pyramid", required = false)
        String pyramid

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = true)
        String bounds

        @Option(name = "-z", aliases = "--zoom-level", usage = "The tile zoom level", required = true)
        Long z

    }

}
