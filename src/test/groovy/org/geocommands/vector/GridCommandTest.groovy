package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.workspace.Property
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.GridCommand.GridOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The GridCommand Unit Test
 * @author Jared Erickson
 */
class GridCommandTest extends BaseTest {

    @Test
    void executeColsRowsToShapefile() {
        File file = createTemporaryShapefile("grid")
        GridCommand cmd = new GridCommand()
        GridOptions options = new GridCommand.GridOptions(
                columns: 2,
                rows: 2,
                geometry: "0 0 10 10",
                outputWorkspace: file.absolutePath,
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(file)
        assertEquals 4, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ
    }

    @Test
    void executeCellWidthHeightToPropertyFile() {
        File file = createTemporaryFile("grid","properties")
        GridCommand cmd = new GridCommand()
        GridOptions options = new GridCommand.GridOptions(
                cellWidth: 5,
                cellHeight: 5,
                geometry: "0 0 10 10",
                outputWorkspace: file.absolutePath,
                type: "point",
                projection: "EPSG:4326"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Property(file.absoluteFile).get(file.name)
        assertEquals 4, layer.count
        assertEquals "Point", layer.schema.geom.typ
    }

    @Test
    void executeToCsv() {
        GridCommand cmd = new GridCommand()
        GridOptions options = new GridCommand.GridOptions(
                cellWidth: 5,
                cellHeight: 5,
                geometry: "0 0 10 10",
                type: "point",
                projection: "EPSG:4326"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertEquals "Point", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector grid",
                "-x", "2",
                "-y", "2",
                "-o", shpFile.absolutePath,
                "-g", "0 0 10 10"
        ] as String[])
        Layer layer = new Shapefile(shpFile)
        assertEquals 4, layer.count
        assertEquals "MultiPolygon", layer.schema.geom.typ

        String output = runApp(["vector grid", "-x", "2", "-y", "2", "-g", "0 0 10 10"], "")
        layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
    }

}
