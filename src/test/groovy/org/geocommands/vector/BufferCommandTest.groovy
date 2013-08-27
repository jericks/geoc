package org.geocommands.vector

import geoscript.geom.Polygon
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.workspace.H2
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

/**
 * The BufferCommand Unit Test
 * @author Jared Erickson
 */
class BufferCommandTest extends BaseTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test void execute() {
        BufferCommand cmd = new BufferCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        BufferCommand.BufferOptions options = new BufferCommand.BufferOptions(
            inputWorkspace: file.absolutePath,
            distance: 2,
            outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test void executeToH2() {
        BufferCommand cmd = new BufferCommand()
        File file = getResource("points.properties")
        File h2File = new File(folder.newFolder("h2buffer"), "database.db")
        BufferCommand.BufferOptions options = new BufferCommand.BufferOptions(
            inputWorkspace: file.absolutePath,
            distance: 2,
            outputWorkspace: "dbtype=h2 database=" + h2File.absolutePath,
            outputLayer: "buffered_points"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        H2 h2 = new H2(h2File.name, h2File.parentFile)
        assertEquals 1, h2.names.size()
        Layer layer = h2.get("buffered_points")
        assertNotNull layer
        assertEquals 3, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
        assertTrue layer.schema.has("the_geom")
        assertTrue layer.schema.has("distance")
        assertTrue layer.schema.has("name")
    }

    @Test void executeWithProperty() {
        BufferCommand cmd = new BufferCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        BufferCommand.BufferOptions options = new BufferCommand.BufferOptions(
                inputWorkspace: file.absolutePath,
                distance: "distance",
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test void executeWithCsv() {
        BufferCommand cmd = new BufferCommand()
        BufferCommand.BufferOptions options = new BufferCommand.BufferOptions(
            distance: 2,
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon}
    }

    @Test void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
            "vector buffer",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath,
            "-d", 1
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        String output = runApp(["vector buffer", "-d", "1"], readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon}
    }
}
