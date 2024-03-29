package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.GeobufReader
import geoscript.layer.io.GeobufWriter
import geoscript.layer.io.MvtWriter
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.FromCommand.FromOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

/**
 * The FromCommand Unit Test
 * @author Jared Erickson
 */
class FromCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
                text: file.text,
                format: "kml",
                geometryType: "Point",
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals "Point", shp.schema.geom.typ
        assertEquals 3, shp.count
        shp.eachFeature { f ->
            assertNotNull f.geom
        }
    }

    @Test
    void executeFromKmlToCsv() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
                format: "kml",
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(file.text), writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals "Geometry", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
        }
    }

    @Test
    void executeFromGeobuf() {
        File csvFile = getResource("points.csv")
        Layer pointsLayer = getLayerFromCsv(csvFile.text)
        GeobufWriter geobufWriter = new GeobufWriter()
        File geobufFile = createTemporaryFile("points","geobuf")
        geobufFile.text = geobufWriter.write(pointsLayer)
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
                format: "geobuf",
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(geobufFile.text), writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals "Point", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
        }
    }

    @Test
    void executeFromMvt() {
        File csvFile = getResource("points.csv")
        Layer pointsLayer = getLayerFromCsv(csvFile.text)
        MvtWriter mvtWriter = new MvtWriter()
        File mvtFile = createTemporaryFile("points","mvt")
        mvtFile.text = mvtWriter.write(pointsLayer)
        FromCommand cmd = new FromCommand()
        FromOptions options = new FromOptions(
                format: "mvt",
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(mvtFile.text), writer)
        Layer layer = getLayerFromCsv(writer.toString())
        assertEquals "Point", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
        }
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.kml")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector from",
                "-t", file.text,
                "-f", "kml",
                "-g", "Point",
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals "Point", shp.schema.geom.typ
        assertEquals 3, shp.count
        shp.eachFeature { f ->
            assertNotNull f.geom
        }

        String output = runApp(["vector from", "-f", "kml"], file.text)
        Layer layer = getLayerFromCsv(output)
        assertEquals "Geometry", layer.schema.geom.typ
        assertEquals 3, layer.count
        layer.eachFeature { f ->
            assertNotNull f.geom
        }
    }

    @Test void runCsv() {
        String actual = runApp([
                "vector from",
                "-f", "csv",
                "-p", "type=XY",
                "-p", "xColumn=x",
                "-p", "yColumn=y"
        ], """"x","y","distance","name"
"1.0","1.0","2","Number 1"
"10.0","10.0","1","Number 2"
"2.0","8.0","5","Number 3"
""")
        String expected = """"x:String","y:String","distance:String","name:String","geom:Point"
"1.0","1.0","2","Number 1","POINT (1 1)"
"10.0","10.0","1","Number 2","POINT (10 10)"
"2.0","8.0","5","Number 3","POINT (2 8)"
"""
        assertStringsEqual actual, expected
    }
}
