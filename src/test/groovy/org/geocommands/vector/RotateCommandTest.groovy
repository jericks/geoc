package org.geocommands.vector

import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.RotateCommand.RotateOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The RotateCommand Unit Test
 * @author Jared Erickson
 */
class RotateCommandTest extends BaseTest {

    @Test
    void executeThetaWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        RotateCommand cmd = new RotateCommand()
        RotateOptions options = new RotateOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                theta: org.locationtech.jts.algorithm.Angle.toRadians(45)
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
            assertEquals inGeom.rotate(org.locationtech.jts.algorithm.Angle.toRadians(45)), outGeom
        }
    }

    @Test
    void executeThetaXYWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        RotateCommand cmd = new RotateCommand()
        RotateOptions options = new RotateOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                theta: org.locationtech.jts.algorithm.Angle.toRadians(45),
                x: "getX(centroid(geom))",
                y: "getY(centroid(geom))"
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
            assertEquals inGeom.rotate(org.locationtech.jts.algorithm.Angle.toRadians(45),
                    inGeom.centroid.x, inGeom.centroid.y), outGeom
        }
    }

    @Test
    void executeSineCosineWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        RotateCommand cmd = new RotateCommand()
        RotateOptions options = new RotateOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                sin: org.locationtech.jts.algorithm.Angle.toRadians(45),
                cos: org.locationtech.jts.algorithm.Angle.toRadians(15),
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
            assertEquals inGeom.rotate(
                    org.locationtech.jts.algorithm.Angle.toRadians(45),
                    org.locationtech.jts.algorithm.Angle.toRadians(15)), outGeom
        }
    }

    @Test
    void executeSineCosineXYWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        RotateCommand cmd = new RotateCommand()
        RotateOptions options = new RotateOptions(
                inputWorkspace: inFile.absolutePath,
                outputWorkspace: outFile,
                sin: org.locationtech.jts.algorithm.Angle.toRadians(45),
                cos: org.locationtech.jts.algorithm.Angle.toRadians(15),
                x: "getX(centroid(geom))",
                y: "getY(centroid(geom))"
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
            assertEquals inGeom.rotate(
                    org.locationtech.jts.algorithm.Angle.toRadians(45),
                    org.locationtech.jts.algorithm.Angle.toRadians(15),
                    inGeom.centroid.x, inGeom.centroid.y), outGeom
        }
    }


    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polys.csv")
        StringWriter writer = new StringWriter()
        RotateCommand cmd = new RotateCommand()
        RotateOptions options = new RotateOptions(
                theta: org.locationtech.jts.algorithm.Angle.toRadians(45),
                x: "getX(centroid(geom))",
                y: "getY(centroid(geom))"
        )
        cmd.execute(options, reader, writer)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("polys.csv").text)
        Layer outLayer = csvReader.read(writer.toString())
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.rotate(org.locationtech.jts.algorithm.Angle.toRadians(45),
                    inGeom.centroid.x, inGeom.centroid.y), outGeom
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("polys.properties")
        File outFile = createTemporaryShapefile("polys")
        runApp([
                "vector rotate",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-t", org.locationtech.jts.algorithm.Angle.toRadians(45),
                "-x", "getX(centroid(geom))",
                "-y", "getY(centroid(geom))"
        ], "")
        Layer inLayer = new Property(inFile)
        Layer outLayer = new Shapefile(outFile)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.rotate(org.locationtech.jts.algorithm.Angle.toRadians(45),
                    inGeom.centroid.x, inGeom.centroid.y), outGeom
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("polys.csv")
        String result = runApp([
                "vector Rotate",
                "-t", org.locationtech.jts.algorithm.Angle.toRadians(45),
                "-x", "getX(centroid(geom))",
                "-y", "getY(centroid(geom))"
        ], reader.text)
        CsvReader csvReader = new CsvReader()
        Layer inLayer = csvReader.read(getStringReader("polys.csv").text)
        Layer outLayer = csvReader.read(result)
        assertEquals inLayer.count, outLayer.count
        List inGeoms = inLayer.collectFromFeature { it.geom }
        List outGeoms = outLayer.collectFromFeature { it.geom }
        (0..<inGeoms.size()).each { i ->
            Geometry inGeom = inGeoms[i]
            Geometry outGeom = outGeoms[i]
            assertEquals inGeom.rotate(org.locationtech.jts.algorithm.Angle.toRadians(45),
                    inGeom.centroid.x, inGeom.centroid.y), outGeom
        }
    }
}
