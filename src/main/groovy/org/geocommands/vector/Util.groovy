package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import geoscript.workspace.Memory
import geoscript.workspace.Workspace

/**
 * A suite of Vector Utilities
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

    /**
     * Check the Schema for valid Geometry type.  If the output Workspace is
     * a Shapefile, it can't be GEOMETRY or GEOMETRYCOLLECTION so change it to
     * the first Feature's Geometry type
     * @param schema The Schema
     * @param inLayer The input Layer
     * @param outputWorkspace The output Workspace
     * @return A Schema
     */
    static Schema checkSchema(Schema schema, Layer inLayer, String outputWorkspace) {
        if ((inLayer.schema.geom.typ.equalsIgnoreCase("Geometry")
                || inLayer.schema.geom.typ.equalsIgnoreCase("GeometryCollection"))
                && Util.isWorkspaceStringShapefile(outputWorkspace)) {
            schema = schema.changeGeometryType(inLayer.first().geom.geometryType, schema.name)
        }
        schema
    }

    /**
     * Get the input Layer from a Workspace string and layer name of from the Reader
     * @param workspaceStr The Workspace String
     * @param layerName The Layer name
     * @param reader The Reader
     * @return A Layer
     */
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

    /**
     * Get the output Layer from the input Layer, output Workspace string, output Layer name.  The last parameter is a
     * Closure that creates the output Schema given the input Layer and output Layer.
     * @param inputLayer The input Layer
     * @param outputWorkspace The output Workspace String
     * @param outputLayer The output Layer name
     * @param createOutputSchema The Closure that creates the output Schema.  THe input Layer and output Layer name are passed
     * in as arguments.
     * @return
     */
    static Layer getOutputLayer(Layer inputLayer, String outputWorkspace, String outputLayer, Closure createOutputSchema = {
        Layer layer, String outLayer -> new Schema(outLayer ? outLayer : layer.name, layer.schema.fields) }
    ) {
        Workspace workspace
        if (!outputWorkspace) {
            workspace = new Memory()
        } else {
            workspace = new Workspace(outputWorkspace)
        }
        workspace.create(createOutputSchema.call(inputLayer, outputLayer))
    }

    /**
     * Get the other Layer from the Workspace string and Layer name
     * @param workspaceStr The Workspace string
     * @param layerName The layer name
     * @return A Layer
     */
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

    /**
     * Get the output Layer name from the output Workspace string, output layer name, and a default name
     * @param outputWorkspace The output workspace string
     * @param outputLayer The output layer name
     * @param defaultName THe default layer name
     * @return
     */
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
