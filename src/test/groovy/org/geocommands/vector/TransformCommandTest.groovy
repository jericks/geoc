package org.geocommands.vector

import geoscript.geom.MultiPolygon
import geoscript.geom.Polygon
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.TransformCommand.TransformOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The TransformCommand Unit Test
 * @author Jared Erickson
 */
class TransformCommandTest extends BaseTest {

    @Test
    void execute() {
        TransformCommand cmd = new TransformCommand()
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("polygons")
        TransformOptions options = new TransformOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile.absolutePath,
                definitions: [
                        the_geom: "buffer(the_geom, 10)",
                        name: "strToUpperCase(name)",
                        distance: "distance * 10"
                ]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        Layer layer = new Property(file)
        List features1 = layer.features
        List features2 = shp.features
        // 1
        assertTrue(features2[0].geom instanceof MultiPolygon)
        assertEquals(features1[0].get("name").toString().toUpperCase(), features2[0].get("name"))
        assertEquals((features1[0].get("distance") as double) * 10, features2[0].get("distance") as double, 0.1)
        // 2
        assertTrue(features2[1].geom instanceof MultiPolygon)
        assertEquals(features1[1].get("name").toString().toUpperCase(), features2[1].get("name"))
        assertEquals((features1[1].get("distance") as double) * 10, features2[1].get("distance") as double, 0.1)
        // 3
        assertTrue(features2[2].geom instanceof MultiPolygon)
        assertEquals(features1[2].get("name").toString().toUpperCase(), features2[2].get("name"))
        assertEquals((features1[2].get("distance") as double) * 10, features2[2].get("distance") as double, 0.1)
    }

    @Test
    void executeWithCsv() {
        TransformCommand cmd = new TransformCommand()
        TransformOptions options = new TransformOptions(
                definitions: [
                        the_geom: "buffer(the_geom, 10)",
                        name: "strToUpperCase(name)",
                        distance: "distance * 10"
                ]
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon }

        Layer csvLayer = new CsvReader().read(getResource("points.csv"))
        List features1 = csvLayer.features
        List features2 = layer.features
        // 1
        assertTrue(features2[0].geom instanceof Polygon)
        assertEquals(features1[0].get("name").toString().toUpperCase(), features2[0].get("name"))
        assertEquals((features1[0].get("distance") as double) * 10, features2[0].get("distance") as double, 0.1)
        // 2
        assertTrue(features2[1].geom instanceof Polygon)
        assertEquals(features1[1].get("name").toString().toUpperCase(), features2[1].get("name"))
        assertEquals((features1[1].get("distance") as double) * 10, features2[1].get("distance") as double, 0.1)
        // 3
        assertTrue(features2[2].geom instanceof Polygon)
        assertEquals(features1[2].get("name").toString().toUpperCase(), features2[2].get("name"))
        assertEquals((features1[2].get("distance") as double) * 10, features2[2].get("distance") as double, 0.1)

    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector transform",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-d", "the_geom=buffer(the_geom, 10)",
                "-d", "name=strToUpperCase(name)",
                "-d", "distance=distance * 10"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ

        Layer layer = new Property(file)
        List features1 = layer.features
        List features2 = shp.features
        // 1
        assertTrue(features2[0].geom instanceof MultiPolygon)
        assertEquals(features1[0].get("name").toString().toUpperCase(), features2[0].get("name"))
        assertEquals((features1[0].get("distance") as double) * 10, features2[0].get("distance") as double, 0.1)
        // 2
        assertTrue(features2[1].geom instanceof MultiPolygon)
        assertEquals(features1[1].get("name").toString().toUpperCase(), features2[1].get("name"))
        assertEquals((features1[1].get("distance") as double) * 10, features2[1].get("distance") as double, 0.1)
        // 3
        assertTrue(features2[2].geom instanceof MultiPolygon)
        assertEquals(features1[2].get("name").toString().toUpperCase(), features2[2].get("name"))
        assertEquals((features1[2].get("distance") as double) * 10, features2[2].get("distance") as double, 0.1)

        String output = runApp([
                "vector transform",
                "-d", "the_geom=buffer(the_geom, 10)",
                "-d", "name=strToUpperCase(name)",
                "-d", "distance=distance * 10"
        ], readCsv("points.csv").text)
        layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        layer.eachFeature { assertTrue it.geom instanceof Polygon }

        Layer csvLayer = new CsvReader().read(getResource("points.csv"))
        features1 = csvLayer.features
        features2 = layer.features
        // 1
        assertTrue(features2[0].geom instanceof Polygon)
        assertEquals(features1[0].get("name").toString().toUpperCase(), features2[0].get("name"))
        assertEquals((features1[0].get("distance") as double) * 10, features2[0].get("distance") as double, 0.1)
        // 2
        assertTrue(features2[1].geom instanceof Polygon)
        assertEquals(features1[1].get("name").toString().toUpperCase(), features2[1].get("name"))
        assertEquals((features1[1].get("distance") as double) * 10, features2[1].get("distance") as double, 0.1)
        // 3
        assertTrue(features2[2].geom instanceof Polygon)
        assertEquals(features1[2].get("name").toString().toUpperCase(), features2[2].get("name"))
        assertEquals((features1[2].get("distance") as double) * 10, features2[2].get("distance") as double, 0.1)
    }

}
