package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.FilterCommand.FilterOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The FilterCommand Unit Test
 * @author Jared Erickson
 */
class FilterCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        FilterCommand cmd = new FilterCommand()
        FilterOptions options = new FilterOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                filter: "col = 1"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        assertEquals 2, shp.count("col = 1")
        assertEquals 0, shp.count("col = 2")
    }

    @Test
    void executeWithCsv() {
        FilterCommand cmd = new FilterCommand()
        FilterOptions options = new FilterOptions(filter: "row = 1")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 2, layer.count
        assertEquals 2, layer.count("row = 1")
        assertEquals 0, layer.count("row = 0")
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector filter",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", "row = 1"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        assertEquals 2, shp.count("row = 1")
        assertEquals 0, shp.count("row = 0")

        String output = runApp(["vector filter", "-f", "col = 1"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 2, layer.count
        assertEquals 2, layer.count("col = 1")
        assertEquals 0, layer.count("col = 0")
    }

}
