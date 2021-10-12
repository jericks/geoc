package org.geocommands.vector

import geoscript.workspace.GeoPackage
import org.geocommands.vector.RemoveLayerCommand.RemoveLayerOptions
import geoscript.feature.Field
import geoscript.workspace.Directory
import geoscript.workspace.Workspace
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Created by jericks on 1/1/16.
 */
class RemoveLayerCommandTest extends BaseTest {

    @Test void execute() {
        RemoveLayerCommand cmd = new RemoveLayerCommand()
        File dir = createDir("shps")
        Workspace workspace = new Directory(dir)
        workspace.create("points",[new Field("geom","Point","EPSG:4326")])
        workspace.create("lines",[new Field("geom","LineString","EPSG:4326")])
        workspace.create("polygons",[new Field("geom","Polygon","EPSG:4326")])

        File file = new File(dir, "points.shp")
        assertTrue file.exists()
        cmd.execute(new RemoveLayerOptions(
                inputWorkspace: file.absolutePath
        ))
        assertFalse file.exists()

        file = new File(dir, "lines.shp")
        assertTrue file.exists()
        cmd.execute(new RemoveLayerOptions(
                inputWorkspace: new File(dir, "lines.shp")
        ))
        assertFalse file.exists()

        file = new File(dir, "polygons.shp")
        assertTrue file.exists()
        cmd.execute(new RemoveLayerOptions(
                inputWorkspace: new File(dir, "polygons.shp")
        ))
        assertFalse file.exists()
    }

    @Test void run() {
        File file = new File(folder, "layers.gpkg")
        Workspace workspace = new GeoPackage(file)
        workspace.create("points",[new Field("geom","Point","EPSG:4326")])
        workspace.create("lines",[new Field("geom","LineString","EPSG:4326")])
        workspace.create("polygons",[new Field("geom","Polygon","EPSG:4326")])

        assertTrue workspace.has("points")
        runApp([
                "vector remove layer",
                "-i", file.absolutePath,
                "-l", "points"
        ],"")
        assertFalse workspace.has("points")

        assertTrue workspace.has("lines")
        runApp([
                "vector remove layer",
                "-i", file.absolutePath,
                "-l", "lines"
        ],"")
        assertFalse workspace.has("lines")

        assertTrue workspace.has("polygons")
        runApp([
                "vector remove layer",
                "-i", file.absolutePath,
                "-l", "polygons"
        ],"")
        assertFalse workspace.has("polygons")
    }
}
