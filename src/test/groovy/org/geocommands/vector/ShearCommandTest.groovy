package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.ShearCommand.ShearOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The ShearCommand Unit Test
 * @author Jared Erickson
 */
class ShearCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        ShearCommand cmd = new ShearCommand()
        ShearOptions options = new ShearOptions(
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
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.shear(5, 10), outGeom
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        ShearCommand cmd = new ShearCommand()
        ShearOptions options = new ShearOptions(
                x: "parseDouble(distance)",
                y: "parseDouble(distance) * 2"
        )
        cmd.execute(options, reader, writer)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("polys.csv").text)
        Layer outLayer = csvReader.read(writer.toString())
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        (0..<inFeatures.size()).each { i ->
            Feature inFeature = inFeatures[i]
            Feature outFeature = outFeatures[i]
            double x = Expression.fromCQL("parseDouble(distance)").evaluate(inFeature) as double
            double y = Expression.fromCQL("parseDouble(distance) * 2").evaluate(inFeature) as double
            assertEquals inFeature.geom.shear(x, y), outFeature.geom
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("points.properties")
        File outFile = createTemporaryShapefile("points")
        runApp([
                "vector Shear",
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
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.shear(5, 10), outGeom
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("polys.csv")
        String result = runApp([
                "vector Shear",
                "-x", "parseDouble(distance)",
                "-y", "parseDouble(distance) * 2"
        ], reader.text)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("polys.csv").text)
        Layer outLayer = csvReader.read(result)
        assertEquals inLayer.count, outLayer.count
        List inFeatures = inLayer.features
        List outFeatures = outLayer.features
        (0..<inFeatures.size()).each { i ->
            Feature inFeature = inFeatures[i]
            Feature outFeature = outFeatures[i]
            double x = Expression.fromCQL("parseDouble(distance)").evaluate(inFeature) as double
            double y = Expression.fromCQL("parseDouble(distance) * 2").evaluate(inFeature) as double
            assertEquals inFeature.geom.shear(x, y), outFeature.geom
        }
    }
}
