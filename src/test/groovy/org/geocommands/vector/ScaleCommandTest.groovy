package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.filter.Expression
import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.ScaleCommand.ScaleOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The ScaleCommand Unit Test
 * @author Jared Erickson
 */
class ScaleCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        ScaleCommand cmd = new ScaleCommand()
        ScaleOptions options = new ScaleOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                xDistance: 5,
                yDistance: 4
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
            assertEquals inGeom.scale(5, 4), outGeom
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        ScaleCommand cmd = new ScaleCommand()
        ScaleOptions options = new ScaleOptions(
                xDistance: "parseDouble(id)",
                yDistance: "parseDouble(id) * 2",
                xCoord: "getX(centroid(geom))",
                yCoord: "getY(centroid(geom))"
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
            double xDist = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double yDist = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            double xCoord = Expression.fromCQL("getX(centroid(geom))").evaluate(inFeature) as double
            double yCoord = Expression.fromCQL("getY(centroid(geom))").evaluate(inFeature) as double
            assertEquals inFeature.geom.scale(xDist, yDist, xCoord, yCoord), outFeature.geom
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        runApp([
                "vector scale",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-x", "5",
                "-y", "5",
                "-c", "getX(centroid(geom))",
                "-d", "getY(centroid(geom))",
        ], "")
        Layer inLayer = new Property(inFile)
        Layer outLayer = new Shapefile(outFile)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.scale(5, 5, inGeom.centroid.x, inGeom.centroid.y), outGeom.getGeometryN(0)
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("polys.csv")
        String result = runApp([
                "vector scale",
                "-x", "parseDouble(id)",
                "-y", "parseDouble(id) * 2"
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
            double x = Expression.fromCQL("parseDouble(id)").evaluate(inFeature) as double
            double y = Expression.fromCQL("parseDouble(id) * 2").evaluate(inFeature) as double
            assertEquals inFeature.geom.scale(x, y), outFeature.geom
        }
    }
}
