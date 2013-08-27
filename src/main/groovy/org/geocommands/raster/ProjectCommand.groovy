package org.geocommands.raster

import geoscript.layer.Raster
import geoscript.proj.Projection
import org.kohsuke.args4j.Option

/**
 * Project a Raster
 * @author Jared Erickson
 */
class ProjectCommand extends RasterInOutCommand<ProjectOptions> {

    @Override
    String getName() {
        "raster project"
    }

    @Override
    String getDescription() {
        "Project a Raster"
    }

    @Override
    ProjectOptions getOptions() {
        new ProjectOptions()
    }

    @Override
    Raster createOutputRaster(Raster inRaster, ProjectOptions options, Reader reader, Writer writer) throws Exception {
        inRaster.reproject(new Projection(options.outputProjection))
    }

    static class ProjectOptions extends RasterInOutOptions {

        @Option(name = "-t", aliases = "--target-projection", usage = "The target projection", required = true)
        String outputProjection

    }
}
