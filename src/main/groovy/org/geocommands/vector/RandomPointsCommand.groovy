package org.geocommands.vector

import geoscript.feature.Field
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Create a new Layer with randomly placed points
 * @author Jared Erickson
 */
class RandomPointsCommand extends LayerOutCommand<RandomPointsOptions> {


    @Override
    String getName() {
        "vector randompoints"
    }

    @Override
    String getDescription() {
        "Create a new Layer with randomly placed points"
    }

    @Override
    RandomPointsOptions getOptions() {
        new RandomPointsOptions()
    }

    @Override
    Layer createLayer(RandomPointsOptions options, Reader reader, Writer writer) throws Exception {
        // Create Layer
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(options.outputWorkspace)
        }
        Layer layer = workspace.create(getOutputLayerName(options, "random_points"), [
                new Field(options.idFieldName, "int"),
                new Field(options.geometryFieldName, "Point", options.projection)
        ])

        // Generate random points
        Geometry randomPoints
        Geometry geom = Geometry.fromString(options.geometry ?: reader.text)
        if (!options.grid) {
            randomPoints = Geometry.createRandomPoints(geom, options.number)
        } else {
            randomPoints = Geometry.createRandomPointsInGrid(geom.bounds, options.number, options.constrainedToCirlce, options.gutterFraction)
        }

        // Add randomly placed points
        layer.withWriter { geoscript.layer.Writer w ->
            randomPoints.geometries.eachWithIndex { Geometry g, int i ->
                w.add(layer.schema.feature([(options.idFieldName): i, (options.geometryFieldName): g]))
            }
        }

        layer
    }

    static class RandomPointsOptions extends LayerOutOptions {

        @Option(name = "-n", aliases = "--number", usage = "The number of points", required = true)
        int number

        @Option(name = "-p", aliases = "--projection", usage = "The projection", required = false)
        String projection = "EPSG:4326"

        @Option(name = "-g", aliases = "--geometry", usage = "The geometry", required = false)
        String geometry

        @Option(name = "-d", aliases = "--grid", usage = "Whether to create random points in grid", required = false)
        boolean grid

        @Option(name = "-c", aliases = "--constrained-to-circle", usage = "Whether the points should be constrained to a circle or not", required = false)
        boolean constrainedToCircle

        @Option(name = "-f", aliases = "--gutter-fraction", usage = "The size of the gutter between cells", required = false)
        double gutterFraction

        @Option(name = "-e", aliases = "--geom-fieldname", usage = "The geometry field name", required = false)
        String geometryFieldName = "the_geom"

        @Option(name = "-u", aliases = "--id-fieldname", usage = "The id field name", required = false)
        String idFieldName = "id"

    }
}
