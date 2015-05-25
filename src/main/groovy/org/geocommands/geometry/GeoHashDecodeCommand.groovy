package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.index.GeoHash
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Decode a GeoHash to a Geometry.
 * @author Jared Erickson
 */
class GeoHashDecodeCommand extends Command<GeoHashDecodeOptions> {

    @Override
    String getName() {
        "geometry geohash decode"
    }

    @Override
    String getDescription() {
        "Decode a GeoHash to a Geometry."
    }

    @Override
    GeoHashDecodeOptions getOptions() {
        new GeoHashDecodeOptions()
    }

    @Override
    void execute(GeoHashDecodeOptions options, Reader reader, Writer writer) throws Exception {
        String hash = options.input ? options.input : reader.text
        GeoHash geohash = new GeoHash()
        Geometry g
        if (hash.isLong()) {
            if (options.type.equalsIgnoreCase("bounds")) {
                g = geohash.decodeBounds(Long.parseLong(hash)).geometry
            } else {
                g = geohash.decode(Long.parseLong(hash))
            }
        } else {
            if (options.type.equalsIgnoreCase("bounds")) {
                g = geohash.decodeBounds(hash).geometry
            } else {
                g = geohash.decode(hash)
            }
        }
        writer.write(g.wkt)
    }

    static class GeoHashDecodeOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geohash", required = false)
        String input

        @Option(name = "-t", aliases = "--type", usage = "Whether the geohash is a point or bounds", required = false)
        String type = "point"

    }
}
