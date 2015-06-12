package org.geocommands.geometry

import geoscript.geom.Bounds
import geoscript.index.GeoHash
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Calculate the geohashes for the given bounds.
 * @author Jared Erickson
 */
class GeoHashBoundsCommand extends Command<GeoHashBoundsOptions> {

    @Override
    String getName() {
        "geometry geohash bounds"
    }

    @Override
    String getDescription() {
        "Calculate the geohashes for the given bounds"
    }

    @Override
    GeoHashBoundsOptions getOptions() {
        new GeoHashBoundsOptions()
    }

    @Override
    void execute(GeoHashBoundsOptions options, Reader reader, Writer writer) throws Exception {
        Bounds bounds = Bounds.fromString(options.bounds ?: reader.text)
        GeoHash geohash = new GeoHash()
        List hashes
        if (options.type.equalsIgnoreCase("string")) {
            hashes = geohash.bboxes(bounds, options.numberOfChars)
        } else {
            hashes = geohash.bboxesLong(bounds, options.bitDepth)
        }
        String NEW_LINE = System.getProperty("line.separator")
        hashes.eachWithIndex {Object hash, int index ->
            if (index > 0) writer.write(NEW_LINE)
            writer.write("${hash}")
        }
    }

    static class GeoHashBoundsOptions extends Options {

        @Option(name = "-b", aliases = "--bounds", usage = "The input geometry", required = false)
        String bounds

        @Option(name = "-t", aliases = "--type", usage = "The encoding type (string or long). The default is string.", required = false)
        String type = "string"

        @Option(name = "-n", aliases = "--number-of-chars", usage = "The number of characters. The default is 9.", required = false)
        int numberOfChars = 9

        @Option(name = "-d", aliases = "--bit-depth", usage = "The bit depth. The default is 52.", required = false)
        int bitDepth = 52

    }

}
