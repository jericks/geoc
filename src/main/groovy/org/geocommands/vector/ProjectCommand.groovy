package org.geocommands.vector

import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.proj.Projection
import org.kohsuke.args4j.Option

/**
 * Project the input Layer to another Projection and save it as the output Layer.
 * @author Jared Erickson
 */
class ProjectCommand extends LayerInOutCommand<ProjectOptions> {

    @Override
    String getName() {
        "vector project"
    }

    @Override
    String getDescription() {
        "Project the input Layer to another Projection and save it as the output Layer."
    }

    @Override
    ProjectOptions getOptions() {
        new ProjectOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, ProjectOptions options, Reader reader, Writer writer) throws Exception {
        if (options.sourceProjection) {
            inLayer.proj = new Projection(options.sourceProjection)
        }
        if (options.targetProjection) {
            outLayer.proj = new Projection(options.targetProjection)
        }
        inLayer.reproject(outLayer)
    }

    @Override
    protected Schema createOutputSchema(Layer layer, ProjectOptions options) {
        layer.schema.reproject(options.targetProjection, getOutputLayerName(layer, options.targetProjection.replace(":", "_"), options))
    }

    static class ProjectOptions extends LayerInOutOptions {
        @Option(name = "-s", aliases = "--source-projection", usage = "The source projection", required = false)
        String sourceProjection

        @Option(name = "-t", aliases = "--target-projection", usage = "The target projection", required = true)
        String targetProjection
    }
}
