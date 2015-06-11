package org.geocommands.geometry

import geoscript.index.GeoHash
import geoscript.index.GeoHash.Direction
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get a geohash's neighbors.
 * @author Jared Erickson
 */
class GeoHashNeighborsCommand extends Command<GeoHashNeighborsOptions> {

    @Override
    String getName() {
        "geometry geohash neighbors"
    }

    @Override
    String getDescription() {
        "Get a geohash's neighbors"
    }

    @Override
    GeoHashNeighborsOptions getOptions() {
        new GeoHashNeighborsOptions()
    }

    @Override
    void execute(GeoHashNeighborsOptions options, Reader reader, Writer writer) throws Exception {
        String hashString = options.input ?: reader.text
        GeoHash geohash = new GeoHash()
        Map<Direction, String> neighbors
        if (!hashString.isLong()) {
            neighbors = geohash.neighbors(hashString, options.numberOfChars)
        } else {
            neighbors = geohash.neighbors(Long.parseLong(hashString), options.bitDepth)
        }
        String NEW_LINE = System.getProperty("line.separator")
        neighbors.eachWithIndex {Direction direction, Object hash, int index ->
            if (index > 0) writer.write(NEW_LINE)
            writer.write(direction.toString() + "," + hash)
        }
    }

    static class GeoHashNeighborsOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geometry", required = false)
        String input

        @Option(name = "-n", aliases = "--number-of-chars", usage = "The number of characters. The default is 9.", required = false)
        int numberOfChars = 9

        @Option(name = "-d", aliases = "--bit-depth", usage = "The bit depth. The default is 52.", required = false)
        int bitDepth = 52

    }

}
