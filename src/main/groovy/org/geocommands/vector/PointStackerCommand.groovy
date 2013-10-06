package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Bounds
import geoscript.layer.Cursor
import geoscript.layer.Layer
import geoscript.process.Process
import geoscript.workspace.Memory
import org.kohsuke.args4j.Option

/**
 * Group nearby points together
 * @author Jared Erickson
 */
class PointStackerCommand extends LayerInOutCommand<PointStackerOptions> {

    @Override
    String getName() {
        "vector pointstacker"
    }

    @Override
    String getDescription() {
        "Group nearby points together"
    }

    @Override
    PointStackerOptions getOptions() {
        new PointStackerOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, PointStackerOptions options, Reader reader, Writer writer) throws Exception {
        // PointStacker requires the input Layer to have a valid projection
        if (!inLayer.proj) {
            Memory workspace = new Memory()
            Layer projectedLayer = workspace.create(inLayer.schema.reproject(options.sourceProjection, "${inLayer.name}_projected"))
            projectedLayer.add(inLayer.cursor)
            inLayer = projectedLayer
        }
        Process process = new Process("vec:PointStacker")
        Map results = process.execute([
                data: inLayer,
                cellSize: options.cellSize,
                outputWidth: options.width,
                outputHeight: options.height,
                outputBBOX: Bounds.fromString(options.bounds) ?: inLayer.bounds
        ])
        Cursor cursor = results.result
        outLayer.withWriter {geoscript.layer.Writer w ->
            cursor.each{f ->
                w.add(f)
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer layer, PointStackerOptions options) {
        new Schema(getOutputLayerName(layer, name, options), [
                new Field("geom", "Point", layer.proj),
                new Field("count", "int"),
                new Field("countUnique", "int")
        ])
    }

    static class PointStackerOptions extends LayerInOutOptions {

        @Option(name = "-c", aliases = "--cell-size", usage = "The cell size in pixels which aggregates points", required = false)
        int cellSize = 1

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-w", aliases = "--width", usage = "The output width", required = true)
        int width

        @Option(name = "-h", aliases = "--height", usage = "The output height", required = true)
        int height

        @Option(name = "-s", aliases = "--source-projection", usage = "The source projection", required = false)
        String sourceProjection = "EPSG:4326"
    }
}
