package org.geocommands.vector

import geoscript.feature.Field
import geoscript.geom.Geometry
import geoscript.index.STRtree
import geoscript.index.SpatialIndex
import geoscript.layer.Layer
import geoscript.workspace.Memory
import geoscript.workspace.Workspace
import org.kohsuke.args4j.Option

/**
 * Split a Layer into separate Layers based on the Feature from another Layer
 * @author Jared Erickson
 */
class SplitByLayerCommand extends LayerCommand<SplitByLayerOptions> {

    @Override
    String getName() {
        "vector splitbylayer"
    }

    @Override
    String getDescription() {
        "Split a Layer into separate Layers based on the Feature from another Layer"
    }

    @Override
    SplitByLayerOptions getOptions() {
        new SplitByLayerOptions()
    }

    @Override
    protected void processLayer(Layer layer, SplitByLayerOptions options, Reader reader, Writer writer) throws Exception {

        Layer splitLayer = getSplitLayer(options.splitWorkspace, options.splitLayer)

        Field field = splitLayer.schema.get(options.field)

        Workspace workspace = options.outputWorkspace ? new Workspace(options.outputWorkspace) : new Memory()

        String NEW_LINE = System.getProperty("line.separator")

        // Put all of the Features in the input Layer in a spatial index
        SpatialIndex index = new STRtree()
        layer.eachFeature { f ->
            index.insert(f.bounds, f)
        }

        // Iterate through all of the Features in the input Layer
        int i = 0
        splitLayer.eachFeature { f ->
            // Create the new output Layer
            Layer outLayer = workspace.create("${layer.name}_${f.get(field).toString().replaceAll(' ', '_')}", layer.schema.fields)
            outLayer.withWriter { geoscript.layer.Writer w ->
                // See if the Feature intersects with the Bounds of any Feature in the spatial index
                index.query(f.bounds).each { layerFeature ->
                    // Make sure it actually intersects the Geometry of a Feature in the spatial index
                    if (f.geom.intersects(layerFeature.geom)) {
                        // Clip the geometry from the input Layer
                        Geometry intersection = layerFeature.geom.intersection(f.geom)
                        // Create a new Feature and add if to the clipped Layer
                        Map values = layerFeature.attributes
                        values[outLayer.schema.geom.name] = intersection
                        w.add(outLayer.schema.feature(values))
                    }
                }
            }
            if (workspace instanceof Memory) {
                if (i > 0) writer.write(NEW_LINE)
                writer.write(outLayer.name)
                writer.write(NEW_LINE)
                writer.write(new geoscript.layer.io.CsvWriter().write(outLayer))
            }
            i++
        }

    }

    private Layer getSplitLayer(String workspaceStr, String layerName) {
        Layer layer = null
        Workspace workspace = new Workspace(workspaceStr)
        if (layerName) {
            layer = workspace.get(layerName)
        } else {
            if (workspace.names.size() == 1) {
                layer = workspace.get(workspace.names[0])
            } else if (workspaceStr.endsWith(".shp") || workspaceStr.endsWith(".properties")) {
                String fileName = new File(workspaceStr).name
                layer = workspace.get(fileName.substring(0, fileName.lastIndexOf(".")))
            }
        }
        layer
    }

    static class SplitByLayerOptions extends LayerOptions {

        @Option(name = "-s", aliases = "--split-workspace", usage = "The input workspace", required = true)
        String splitWorkspace

        @Option(name = "-p", aliases = "--split-layer", usage = "The input layer", required = false)
        String splitLayer

        @Option(name = "-f", aliases = "--field", usage = "The field name", required = true)
        String field

        @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
        String outputWorkspace

    }
}
