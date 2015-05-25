package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.geom.Point
import geoscript.index.GeoHash
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Encode a Geometry as a GeoHash
 * @author Jared Erickson
 */
class GeoHashEncodeCommand extends Command<GeoHashEncodeOptions> {

    /**
     * Get the name of the command
     * @return The name of the command
     */
    @Override
    String getName() {
        "geometry geohash encode"
    }

    /**
     * Get the short description
     * @return A short description
     */
    @Override
    String getDescription() {
        "Encode a Geometry as a GeoHash"
    }

    /**
     * Get the Options
     * @return The Options
     */
    @Override
    GeoHashEncodeOptions getOptions() {
        new GeoHashEncodeOptions()
    }

    /**
     * Execute the command
     * @param options The Options
     * @param reader The Reader
     * @param writer The Write
     * @throws Exception if an error occurs
     */
    @Override
    void execute(GeoHashEncodeOptions options, Reader reader, Writer writer) throws Exception {
        Geometry geom = Geometry.fromString(options.input ? options.input : reader.text)
        GeoHash geohash = new GeoHash()
        Point pt = geom.centroid
        if (options.type.equalsIgnoreCase("string")) {
            writer.write(geohash.encode(pt, options.numberOfChars))
        } else {
            writer.write(String.valueOf(geohash.encodeLong(pt, options.bitDepth)))
        }
    }

    static class GeoHashEncodeOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geometry", required = false)
        String input

        @Option(name = "-t", aliases = "--type", usage = "The encoding type (string or long). The default is string.", required = false)
        String type = "string"

        @Option(name = "-n", aliases = "--number-of-chars", usage = "The number of characters. The default is 9.", required = false)
        int numberOfChars = 9

        @Option(name = "-d", aliases = "--bit-depth", usage = "The bit depth. The default is 52.", required = false)
        int bitDepth = 52

    }

}