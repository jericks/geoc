package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import org.geocommands.BaseTest
import org.geocommands.vector.SmoothCommand.SmoothOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The SmoothCommand Unit Test
 * @author Jared Erickson
 */
class SmoothCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        SmoothCommand cmd = new SmoothCommand()
        File file = getResource("jagged.properties")
        File shpFile = createTemporaryShapefile("smooth")
        SmoothOptions options = new SmoothOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                fit: 0.5
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer outLayer = new Shapefile(shpFile)
        Layer inLayer = new Property(file)
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        // Each output feature should have more points
        (0..<inFeatures.size()).each { int i ->
            assertTrue(outFeatures[i].geom.numPoints > inFeatures[i].geom.numPoints)
        }
    }

    @Test
    void executeWithText() {
        SmoothCommand cmd = new SmoothCommand()
        SmoothOptions options = new SmoothOptions(fit: 0.75)
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("jagged.csv"), w)
        Layer outLayer = getLayerFromCsv(w.toString())
        Layer inLayer = getLayerFromCsv(readCsv("jagged.csv").text)
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        // Each output feature should have more points
        (0..<inFeatures.size()).each { int i ->
            assertTrue(outFeatures[i].geom.numPoints > inFeatures[i].geom.numPoints)
        }
    }

    @Test
    void runWithFiles() {
        File file = getResource("jagged.properties")
        File shpFile = createTemporaryShapefile("smooth")
        runApp([
                "vector smooth",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-f", 0.40
        ], "")
        Layer outLayer = new Shapefile(shpFile)
        Layer inLayer = new Property(file)
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        // Each output feature should have more points
        (0..<inFeatures.size()).each { int i ->
            assertTrue(outFeatures[i].geom.numPoints > inFeatures[i].geom.numPoints)
        }
    }

    @Test
    void runWithText() {
        String output = runApp([
                "vector smooth",
                "-f", "0.25"
        ], readCsv("jagged.csv").text)
        Layer outLayer = getLayerFromCsv(output)
        Layer inLayer = getLayerFromCsv(readCsv("jagged.csv").text)
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        // Each output feature should have more points
        (0..<inFeatures.size()).each { int i ->
            assertTrue(outFeatures[i].geom.numPoints > inFeatures[i].geom.numPoints)
        }
    }
}