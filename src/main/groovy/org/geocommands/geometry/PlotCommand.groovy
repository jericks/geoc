package org.geocommands.geometry

import geoscript.geom.Geometry
import geoscript.viewer.Viewer
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Draw geometry to a plot
 * @author Jared Erickson
 */
class PlotCommand extends Command<PlotOptions>{

    @Override
    String getName() {
        "geometry plot"
    }

    @Override
    String getDescription() {
        "Draw a geometry to a plot"
    }

    @Override
    PlotOptions getOptions() {
        new PlotOptions()
    }

    @Override
    void execute(PlotOptions options, Reader reader, Writer writer) throws Exception {
        Geometry geom = Geometry.fromString(options.input ? options.input : reader.text)
        Viewer.plotToFile(geom, new File(options.file),
                size: [options.width, options.height],
                legend: options.legend,
                fillCoords: options.fillCoords,
                fillPolys: options.fillPolys,
                drawCoords: options.drawCoords
        )
    }

    static class PlotOptions extends Options {

        @Option(name = "-i", aliases = "--input", usage = "The input geometry", required = false)
        String input

        @Option(name = "-f", aliases = "--file", usage = "The output file", required = false)
        String file = "plot.png"

        @Option(name = "-w", aliases = "--width", usage = "The image width", required = false)
        int width = 400

        @Option(name = "-h", aliases = "--height", usage = "The image height", required = false)
        int height = 400

        @Option(name = "-l", aliases = "--legend", usage = "Whether to show the legend", required = false)
        boolean legend = false

        @Option(name = "-r", aliases = "--fill-coords", usage = "Whether to fill coordinates", required = false)
        boolean fillCoords = false

        @Option(name = "-p", aliases = "--fill-polys", usage = "Whether to fill polygons", required = false)
        boolean fillPolys = false

        @Option(name = "-d", aliases = "--draw-coords", usage = "Whether to draw coordinates", required = false)
        boolean drawCoords = false

    }
}
