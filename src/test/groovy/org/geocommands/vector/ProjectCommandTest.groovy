package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.ProjectCommand.ProjectOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The ProjectCommand Unit Test
 * @author Jared Erickson
 */
class ProjectCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points2927.properties")
        File shpFile = createTemporaryShapefile("points")
        ProjectCommand cmd = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile.absolutePath,
                sourceProjection: "EPSG:2927",
                targetProjection: "EPSG:4326"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ
        assertEquals "EPSG:4326", shp.proj.id
        assertEquals "POINT (-122.36985813333858 47.12497202856968)", shp.features[0].geom.wkt
        assertEquals "POINT (-122.21241010080831 47.01970249424006)", shp.features[1].geom.wkt
        assertEquals "POINT (-122.29601986406784 47.249057682554785)", shp.features[2].geom.wkt
        assertEquals "POINT (-122.77252242180316 47.250825786783665)", shp.features[3].geom.wkt
    }

    @Test
    void executeWithCsv() {
        ProjectCommand cmd = new ProjectCommand()
        ProjectOptions options = new ProjectOptions(
                sourceProjection: "EPSG:2927",
                targetProjection: "EPSG:4326"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points2927.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        assertEquals "POINT (-122.36985813333858 47.12497202856968)", layer.features[0].geom.wkt
        assertEquals "POINT (-122.21241010080831 47.01970249424006)", layer.features[1].geom.wkt
        assertEquals "POINT (-122.29601986406784 47.249057682554785)", layer.features[2].geom.wkt
        assertEquals "POINT (-122.77252242180316 47.250825786783665)", layer.features[3].geom.wkt
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points2927.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector project",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-s", "EPSG:2927",
                "-t", "EPSG:4326"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertEquals "Point", shp.schema.geom.typ
        assertEquals "EPSG:4326", shp.proj.id
        assertEquals "POINT (-122.36985813333858 47.12497202856968)", shp.features[0].geom.wkt
        assertEquals "POINT (-122.21241010080831 47.01970249424006)", shp.features[1].geom.wkt
        assertEquals "POINT (-122.29601986406784 47.249057682554785)", shp.features[2].geom.wkt
        assertEquals "POINT (-122.77252242180316 47.250825786783665)", shp.features[3].geom.wkt

        String output = runApp(["vector project", "-s", "EPSG:2927", "-t", "EPSG:4326"], readCsv("points2927.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        assertEquals "POINT (-122.36985813333858 47.12497202856968)", layer.features[0].geom.wkt
        assertEquals "POINT (-122.21241010080831 47.01970249424006)", layer.features[1].geom.wkt
        assertEquals "POINT (-122.29601986406784 47.249057682554785)", layer.features[2].geom.wkt
        assertEquals "POINT (-122.77252242180316 47.250825786783665)", layer.features[3].geom.wkt
    }

}
