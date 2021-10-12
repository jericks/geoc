package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.TranslateCommand.TranslateOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The TranslateCommand Unit Test
 * @author Jared Erickson
 */
class TranslateCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("points.properties")
        File outFile = createTemporaryShapefile("points")
        TranslateCommand cmd = new TranslateCommand()
        TranslateOptions options = new TranslateOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                x: 5,
                y: 10
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer inLayer = new Property(inFile)
        Layer outLayer = new Shapefile(outFile)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Point inGeom = inGeoms[i]
            Point outGeom = outGeoms[i]
            assertEquals outGeom.x, inGeom.x + 5, 0.1
            assertEquals outGeom.y, inGeom.y + 10, 0.1
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("points.csv")
        StringWriter writer = new StringWriter()
        TranslateCommand cmd = new TranslateCommand()
        TranslateOptions options = new TranslateOptions(
                x: "parseDouble(distance)",
                y: "parseDouble(distance) * 2"
        )
        cmd.execute(options, reader, writer)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("points.csv").text)
        Layer outLayer = csvReader.read(writer.toString())
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Point inGeom = inGeoms[i]
            Point outGeom = outGeoms[i]
            if (i == 0) {
                assertEquals outGeom.x, inGeom.x + 2, 0.1
                assertEquals outGeom.y, inGeom.y + 4, 0.1
            } else if (i == 1) {
                assertEquals outGeom.x, inGeom.x + 1, 0.1
                assertEquals outGeom.y, inGeom.y + 2, 0.1
            } else if (i == 2) {
                assertEquals outGeom.x, inGeom.x + 5, 0.1
                assertEquals outGeom.y, inGeom.y + 10, 0.1
            }
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("points.properties")
        File outFile = createTemporaryShapefile("points")
        runApp([
                "vector translate",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-x", "5",
                "-y", "10"
        ], "")
        Layer inLayer = new Property(inFile)
        Layer outLayer = new Shapefile(outFile)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Point inGeom = inGeoms[i]
            Point outGeom = outGeoms[i]
            assertEquals outGeom.x, inGeom.x + 5, 0.1
            assertEquals outGeom.y, inGeom.y + 10, 0.1
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("points.csv")
        String result = runApp([
                "vector translate",
                "-x", "parseDouble(distance)",
                "-y", "parseDouble(distance) * 2"
        ], reader.text)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("points.csv").text)
        Layer outLayer = csvReader.read(result)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Point inGeom = inGeoms[i]
            Point outGeom = outGeoms[i]
            if (i == 0) {
                assertEquals outGeom.x, inGeom.x + 2, 0.1
                assertEquals outGeom.y, inGeom.y + 4, 0.1
            } else if (i == 1) {
                assertEquals outGeom.x, inGeom.x + 1, 0.1
                assertEquals outGeom.y, inGeom.y + 2, 0.1
            } else if (i == 2) {
                assertEquals outGeom.x, inGeom.x + 5, 0.1
                assertEquals outGeom.y, inGeom.y + 10, 0.1
            }
        }
    }
}
