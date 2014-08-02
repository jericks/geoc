package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert a text stream of WKT geometries to a Layer
 * @author Jared Erickson
 */
class GeometryReaderCommand extends Command<GeometryReaderOptions> {

    private final String NEW_LINE = System.getProperty("line.separator")

    @Override
    String getName() {
        "vector geomr"
    }

    @Override
    String getDescription() {
        "Convert a text stream of WKT geometries to a Layer"
    }

    @Override
    GeometryReaderOptions getOptions() {
        new GeometryReaderOptions()
    }

    void execute(GeometryReaderOptions options, Reader reader, Writer writer) {
        Layer outLayer
        geoscript.layer.Writer w
        try {
            int i = 0
            if (options.text != null) {
                reader = new StringReader(options.text)
            }
            try {
                reader.eachLine { line ->
                    if (line.trim().length() > 0) {
                        def geom = Geometry.fromWKT(line)
                        if (i == 0) {
                            outLayer = getOutputLayer(geom, options)
                            w = new geoscript.layer.Writer(outLayer)
                        }
                        i++
                        w.add(outLayer.schema.feature([id: i, the_geom: geom]))
                    }
                }
            } finally {
                w.close()
            }
            if (outLayer.workspace instanceof Memory) {
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
        }
        finally {
            outLayer.workspace.close()
        }
    }

    Layer getOutputLayer(Geometry geom, GeometryReaderOptions options) {
        Workspace workspace
        if (!options.outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(options.outputWorkspace)
        }
        workspace.create(new Schema(getOutputLayerName(options, "geometry"), [new Field("id", "int"), new Field("the_geom", geom.geometryType)]))
    }

    protected String getOutputLayerName(GeometryReaderOptions options, String defaultName) {
        String outName = options.outputLayer ? options.outputLayer : defaultName
        if (options.outputWorkspace && (options.outputWorkspace.endsWith(".shp") || options.outputWorkspace.endsWith(".properties"))) {
            String fileName = new File(options.outputWorkspace).name
            outName = fileName.substring(0, fileName.lastIndexOf("."))
        }
        outName
    }

    static class GeometryReaderOptions extends Options {

        @Option(name = "-t", aliases = "--text", usage = "The text", required = false)
        String text

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace

        @Option(name = "-r", aliases = "--output-layer", usage = "The output layer", required = false)
        String outputLayer

    }
}
