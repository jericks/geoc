package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Cursor
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DissolveByFieldCommand.DissolveByFieldOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The DissolveByFieldCommand Unit Test
 * @author Jared Erickson
 */
class DissolveByFieldCommandTest extends BaseTest {

    @Test void execute() {
        DissolveByFieldCommand cmd = new DissolveByFieldCommand()
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("dissolvebyfield")
        DissolveByFieldOptions options = new DissolveByFieldOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile,
            field: "row"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        Cursor c = shp.cursor
        Feature f = c.next()
        assertEquals 0, f['id']
        assertEquals 2, f['count']
        assertEquals "MULTIPOLYGON (((0 0, 0 5, 5 5, 10 5, 10 0, 5 0, 0 0)))", f.geom.wkt
        f = c.next()
        assertEquals 1, f['id']
        assertEquals 2, f['count']
        assertEquals "MULTIPOLYGON (((0 5, 0 10, 5 10, 10 10, 10 5, 5 5, 0 5)))", f.geom.wkt
    }

    @Test void executeCsv() {
        DissolveByFieldCommand cmd = new DissolveByFieldCommand()
        DissolveByFieldOptions options = new DissolveByFieldOptions(
            field: "row"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 2, layer.count
        Cursor c = layer.cursor
        Feature f = c.next()
        assertEquals 0, f['id'] as Integer
        assertEquals 2, f['count'] as Integer
        assertEquals "POLYGON ((0 0, 0 5, 5 5, 10 5, 10 0, 5 0, 0 0))", f.geom.wkt
        f = c.next()
        assertEquals 1, f['id'] as Integer
        assertEquals 2, f['count'] as Integer
        assertEquals "POLYGON ((0 5, 0 10, 5 10, 10 10, 10 5, 5 5, 0 5))", f.geom.wkt
    }

    @Test void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("dissolvebyfield")
        App.main([
            "vector dissolvebyfield",
            "-i", file.absolutePath,
            "-o", shpFile.absolutePath,
            "-f", "row"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        Cursor c = shp.cursor
        Feature f = c.next()
        assertEquals 0, f['id']
        assertEquals 2, f['count']
        assertEquals "MULTIPOLYGON (((0 0, 0 5, 5 5, 10 5, 10 0, 5 0, 0 0)))", f.geom.wkt
        f = c.next()
        assertEquals 1, f['id']
        assertEquals 2, f['count']
        assertEquals "MULTIPOLYGON (((0 5, 0 10, 5 10, 10 10, 10 5, 5 5, 0 5)))", f.geom.wkt

        String output = runApp(["vector dissolvebyfield", "-f", "row"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 2, layer.count
        c = layer.cursor
        f = c.next()
        assertEquals 0, f['id'] as Integer
        assertEquals 2, f['count'] as Integer
        assertEquals "POLYGON ((0 0, 0 5, 5 5, 10 5, 10 0, 5 0, 0 0))", f.geom.wkt
        f = c.next()
        assertEquals 1, f['id'] as Integer
        assertEquals 2, f['count'] as Integer
        assertEquals "POLYGON ((0 5, 0 10, 5 10, 10 10, 10 5, 5 5, 0 5))", f.geom.wkt
    }

}
