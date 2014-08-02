package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import geoscript.workspace.Memory
import geoscript.workspace.Workspace

/**
 * Layer Utilities
 * @author Jared Erickson
 */
class Util {

    /**
     * Is the Workspace String a Shapefile?
     * @param workspaceStr The Workspace String
     * @return Whether the Workspace String is a Shapefile or not
     */
    static boolean isWorkspaceStringShapefile(String workspaceStr) {
        if (workspaceStr != null && (workspaceStr.endsWith(".shp")
                || workspaceStr.trim().startsWith("url=")
                || workspaceStr.indexOf("=") == -1 && new File(workspaceStr).isDirectory())
        ) {
            return true
        } else {
            return false
        }
    }

    static Schema checkSchema(Schema schema, Layer inLayer, String outputWorkspace) {
        if ((inLayer.schema.geom.typ.equalsIgnoreCase("Geometry")
                || inLayer.schema.geom.typ.equalsIgnoreCase("GeometryCollection"))
                && Util.isWorkspaceStringShapefile(outputWorkspace)) {
            schema = schema.changeGeometryType(inLayer.first().geom.geometryType, schema.name)
        }
        schema
    }

    static Layer getInputLayer(String workspaceStr, String layerName, Reader reader) {
        Layer layer = null
        if (workspaceStr) {
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
        } else if (reader != null) {
            layer = new CsvReader().read(reader.text)
        }
        layer
    }

    static Layer getOutputLayer(Layer inputLayer, String outputWorkspace, String outputLayer,
                                Closure createOutputSchema = { Layer layer, String outLayer -> new Schema(outLayer ? outLayer : layer.name, layer.schema.fields) }) {
        Workspace workspace
        if (!outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(outputWorkspace)
        }
        workspace.create(createOutputSchema.call(inputLayer, outputLayer))
    }

    static Layer getOtherLayer(String workspaceStr, String layerName) {
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

    static String getOutputLayerName(String outputWorkspace, String outputLayer, String defaultName) {
        String outName = outputLayer ? outputLayer : defaultName
        if (outputWorkspace && (outputWorkspace.endsWith(".shp") || outputWorkspace.endsWith(".properties"))) {
            String fileName = new File(outputWorkspace).name
            outName = fileName.substring(0, fileName.lastIndexOf("."))
        }
        outName
    }

    /**
     * Get a Workspace from a workspace string or if the workspace string is null,
     * then return an in Memory Workspace
     * @param workspace The workspace string
     * @return A Workspace
     */
    static Workspace getWorkspace(String workspace) {
        if (!workspace) {
            new Memory()
        } else {
            new Workspace(workspace)
        }
    }

    /**
     * Create an output Layer
     * @param workspace The workspace string
     * @param name The name of the output Layer
     * @param fields A List of Fields for the output Layer
     * @return The output Layer
     */
    static Layer createOutputLayer(String workspace, String name, List<Field> fields) {
        Workspace w = getWorkspace(workspace)
        w.create(name, fields)
    }

}
