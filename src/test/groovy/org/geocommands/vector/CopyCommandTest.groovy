package org.geocommands.vector

import geoscript.feature.Field
import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.workspace.H2
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.CopyCommand.CopyOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The CopyCommand Unit Test
 */
class CopyCommandTest extends BaseTest {

    @Test
    void execute() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        CopyCommand cmd = new CopyCommand()

        // Copy all
        CopyOptions options = new CopyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count

        // Copy filter
        options = new CopyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                filter: "col = 1"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        assertEquals 2, shp.count("col = 1")
        assertEquals 0, shp.count("col = 2")

        // Copy Sort
        options = new CopyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                sort: ["id DESC"]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        List features = shp.features
        assertEquals 3, features[0].get("id")
        assertEquals 2, features[1].get("id")
        assertEquals 1, features[2].get("id")
        assertEquals 0, features[3].get("id")

        // Copy start max
        options = new CopyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                sort: ["id DESC"],
                start: 2,
                max: 2
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        shp = new Shapefile(shpFile)
        assertEquals 2, shp.count
        features = shp.features
        assertEquals 1, features[0].get("id")
        assertEquals 0, features[1].get("id")

        // Copy sub-fields
        options = new CopyOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                fields: ["row", "col"]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        shp = new Shapefile(shpFile)
        assertEquals 4, shp.count
        assertFalse shp.schema.has("id")
        assertTrue shp.schema.has("col")
        assertTrue shp.schema.has("row")
    }

    @Test
    void executeWithCsv() {
        CopyCommand cmd = new CopyCommand()
        CopyOptions options = new CopyOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
    }

    @Test
    void executeH2ToShapefile() {

        // Create an H2 Database with two Layers
        File dir = folder.newFolder("layers")
        File h2File = new File(dir, "layers.db")
        H2 h2 = new H2("layers.db", dir)

        // points Layer with Geometry type Point
        Layer l = h2.create('points', [new Field("geom", "Point", "EPSG:4326"), new Field("name", "String")])
        l.add([new Point(1, 1), "one"])
        l.add([new Point(2, 2), "two"])
        l.add([new Point(3, 3), "three"])

        // geometries Layer with Geometry type Geometry
        l = h2.create('geometries', [new Field("geom", "Geometry", "EPSG:4326"), new Field("name", "String")])
        l.add([new Point(1, 1), "one"])
        l.add([new Point(2, 2), "two"])
        l.add([new Point(3, 3), "three"])

        h2.close()

        // Points
        CopyCommand cmd = new CopyCommand()
        File shpFile = createTemporaryShapefile("points")
        CopyOptions options = new CopyOptions(
                inputWorkspace: "dbtype=h2 database=" + h2File.absolutePath,
                inputLayer: "points",
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 3, shp.count

        // Geometries
        shpFile = createTemporaryShapefile("geometries")
        options = new CopyOptions(
                inputWorkspace: "dbtype=h2 database=" + h2File.absolutePath,
                inputLayer: "geometries",
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        shp = new Shapefile(shpFile)
        assertEquals 3, shp.count
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File shpFile = createTemporaryShapefile("polygons")
        App.main([
                "vector copy",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 4, shp.count

        String output = runApp(["vector copy"], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
    }

}
