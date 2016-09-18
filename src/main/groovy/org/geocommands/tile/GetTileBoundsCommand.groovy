package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.Pyramid
import geoscript.layer.Tile
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Get the Bounds of a tile
 * @author Jared Erickson
 */
class GetTileBoundsCommand extends Command<GetTileBoundsOptions> {

    @Override
    String getName() {
        "tile get bounds"
    }

    @Override
    String getDescription() {
        "Get the Bounds of a tile"
    }

    @Override
    GetTileBoundsOptions getOptions() {
        new GetTileBoundsOptions()
    }

    @Override
    void execute(GetTileBoundsOptions options, Reader reader, Writer writer) throws Exception {
        Pyramid pyramid = Pyramid.fromString(options.pyramid ?: reader.text)
        Bounds b = pyramid.bounds(new Tile(options.z, options.x, options.y))
        writer.write(b.geometry.wkt)
    }

    static class GetTileBoundsOptions extends Options {

        @Option(name = "-p", aliases = "--pyramid", usage = "The tile pyramid", required = false)
        String pyramid

        @Option(name = "-z", aliases = "--zoom-level", usage = "The tile zoom level", required = true)
        Long z

        @Option(name = "-x", aliases = "--column", usage = "The tile x or column", required = true)
        Long x

        @Option(name = "-y", aliases = "--row", usage = "The tile y or row", required = true)
        Long y

    }
}
