package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DelaunayDiagramCommand.DelaunayDiagramOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The DelaunayDiagramCommand Unit Test
 * @author Jared Erickson
 */
class DelaunayDiagramCommandTest extends BaseTest {

    @Test
    void execute() {
        DelaunayDiagramCommand cmd = new DelaunayDiagramCommand()
        File file = getResource("points50.properties")
        File shpFile = createTemporaryShapefile("delaunay")
        DelaunayDiagramOptions options = new DelaunayDiagramOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 86, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        DelaunayDiagramCommand cmd = new DelaunayDiagramCommand()
        DelaunayDiagramOptions options = new DelaunayDiagramOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points50.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals 86, layer.features[0].geom.numGeometries
        assertEquals "MultiPolygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points50.properties")
        File shpFile = createTemporaryShapefile("delaunay")
        App.main([
                "vector delaunay",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 86, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector delaunay"], readCsv("points50.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals 86, layer.features[0].geom.numGeometries
        assertEquals "MultiPolygon", layer.schema.geom.typ
    }
}
