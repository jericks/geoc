package org.geocommands.vector

import geoscript.geom.Geometry
import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.MergeCommand.MergeOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The MergeCommand Unit Test
 * @author Jared Erickson
 */
class MergeCommandTest extends BaseTest {

    @Test
    void execute() {
        File file1 = getResource("polygons.properties")
        File file2 = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("merged")
        MergeCommand cmd = new MergeCommand()
        MergeOptions options = new MergeOptions(
                inputWorkspace: file1.absolutePath,
                otherWorkspace: file2.absolutePath,
                outputWorkspace: shpFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 14, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
        assertTrue shp.schema.has("id1")
        assertTrue shp.schema.has("row1")
        assertTrue shp.schema.has("col1")
        shp.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Geometry
        }
    }

    @Test
    void executeCsv() {
        File file2 = getResource("overlapping.properties")
        MergeCommand cmd = new MergeCommand()
        MergeOptions options = new MergeOptions(
                otherWorkspace: file2.absolutePath,
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 14, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
        assertTrue layer.schema.has("id1")
        assertTrue layer.schema.has("row1")
        assertTrue layer.schema.has("col1")
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Geometry
        }
    }

    @Test
    void runAsCommandLine() {
        File file1 = getResource("polygons.properties")
        File file2 = getResource("overlapping.properties")
        File shpFile = createTemporaryShapefile("merged")
        App.main([
                "vector merge",
                "-i", file1.absolutePath,
                "-k", file2.absolutePath,
                "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 14, shp.count
        assertEquals "MultiPolygon", shp.schema.geom.typ
        assertTrue shp.schema.has("id1")
        assertTrue shp.schema.has("row1")
        assertTrue shp.schema.has("col1")
        shp.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Geometry
        }

        String output = runApp(["vector merge", "-k", file2.absolutePath], readCsv("polygons.csv").text)
        Layer layer = getLayerFromCsv(output)
        assertEquals 14, layer.count
        assertEquals "Polygon", layer.schema.geom.typ
        assertTrue layer.schema.has("id1")
        assertTrue layer.schema.has("row1")
        assertTrue layer.schema.has("col1")
        layer.eachFeature { f ->
            assertNotNull f.geom
            assertTrue f.geom instanceof Geometry
        }
    }
}
