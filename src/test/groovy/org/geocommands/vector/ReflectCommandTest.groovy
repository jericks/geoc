package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.ReflectCommand.ReflectOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The ReflectCommand Unit Test
 * @author Jared Erickson
 */
class ReflectCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        ReflectCommand cmd = new ReflectCommand()
        ReflectOptions options = new ReflectOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                x1: 5,
                y1: 4
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
            assertEquals inGeom.reflect(5, 4), outGeom
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        ReflectCommand cmd = new ReflectCommand()
        ReflectOptions options = new ReflectOptions(
                x1: "parseDouble(id)",
                x2: "parseDouble(id) * 2",
                y1: "parseDouble(id)",
                y2: "parseDouble(id) * 2"
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
            double x1 = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double x2 = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            double y1 = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double y2 = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            assertEquals inFeature.geom.reflect(x1, y1, x2, y2), outFeature.geom
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        runApp([
                "vector reflect",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-x", "5",
                "-y", "5"
        ], "")
        Layer inLayer = new Property(inFile)
        Layer outLayer = new Shapefile(outFile)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.reflect(5, 5), outGeom.getGeometryN(0)
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("polys.csv")
        String result = runApp([
                "vector reflect",
                "-x", "parseDouble(id)",
                "-y", "parseDouble(id)",
                "-c", "parseDouble(id) * 2",
                "-d", "parseDouble(id) * 2"
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
            double x1 = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double y1 = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double x2 = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            double y2 = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            assertEquals inFeature.geom.reflect(x1, y1, x2, y2), outFeature.geom
        }
    }
}
