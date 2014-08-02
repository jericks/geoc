package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Cursor
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.workspace.Property
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.SimplifyCommand.SimplifyOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The SimplifyCommand Unit Test
 * @author Jared Erickson
 */
class SimplifyCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("polygons")
        SimplifyCommand cmd = new SimplifyCommand()
        SimplifyOptions options = new SimplifyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                distanceTolerance: 1.4
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        Layer prop = new Property(file.parentFile).get(file.name)
        assertEquals prop.count, shp.count
        Cursor pCursor = prop.cursor
        Cursor sCursor = shp.cursor
        while (pCursor.hasNext()) {
            Feature pFeature = pCursor.next()
            Feature sFeature = sCursor.next()
            assertTrue pFeature.geom.numPoints > sFeature.geom.numPoints
        }
    }

    @Test
    void executeCsv() {
        SimplifyCommand cmd = new SimplifyCommand()
        SimplifyOptions options = new SimplifyOptions(
                distanceTolerance: 1.4
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("overlapping.csv"), w)
        Layer layer1 = getLayerFromCsv(readCsv("overlapping.csv").text)
        Layer layer2 = getLayerFromCsv(w.toString())
        assertEquals layer1.count, layer2.count
        Cursor cursor1 = layer1.cursor
        Cursor cursor2 = layer2.cursor
        while (cursor1.hasNext()) {
            Feature pFeature = cursor1.next()
            Feature sFeature = cursor2.next()
            assertTrue pFeature.geom.numPoints > sFeature.geom.numPoints
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector simplify",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-d", "2"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        Layer prop = new Property(file.parentFile).get(file.name)
        assertEquals prop.count, shp.count
        Cursor pCursor = prop.cursor
        Cursor sCursor = shp.cursor
        while (pCursor.hasNext()) {
            Feature pFeature = pCursor.next()
            Feature sFeature = sCursor.next()
            assertTrue pFeature.geom.numPoints > sFeature.geom.numPoints
        }

        String output = runApp(["vector simplify", "-d", "2"], readCsv("overlapping.csv").text)
        Layer layer1 = getLayerFromCsv(readCsv("overlapping.csv").text)
        Layer layer2 = getLayerFromCsv(output)
        assertEquals layer1.count, layer2.count
        Cursor cursor1 = layer1.cursor
        Cursor cursor2 = layer2.cursor
        while (cursor1.hasNext()) {
            Feature pFeature = cursor1.next()
            Feature sFeature = cursor2.next()
            assertTrue pFeature.geom.numPoints > sFeature.geom.numPoints
        }
    }

}
