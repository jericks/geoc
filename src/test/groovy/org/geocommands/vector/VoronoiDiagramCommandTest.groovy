package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.VoronoiDiagramCommand.VoronoiDiagramOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The VoronoiDiagramCommand Unit Test
 * @author Jared Erickson
 */
class VoronoiDiagramCommandTest extends BaseTest {

    @Test
    void execute() {
        VoronoiDiagramCommand cmd = new VoronoiDiagramCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("voronoi")
        VoronoiDiagramOptions options = new VoronoiDiagramOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 3, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void executeWithCsv() {
        VoronoiDiagramCommand cmd = new VoronoiDiagramCommand()
        VoronoiDiagramOptions options = new VoronoiDiagramOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 1, layer.count
        assertEquals 3, layer.features[0].geom.numGeometries
        assertEquals "MultiPolygon", layer.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("voronoi")
        App.main([
                "vector voronoi",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 1, shp.count
        assertEquals 3, shp.features[0].geom.numGeometries
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector voronoi"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 1, layer.count
        assertEquals 3, layer.features[0].geom.numGeometries
        assertEquals "MultiPolygon", layer.schema.geom.typ
    }
}
