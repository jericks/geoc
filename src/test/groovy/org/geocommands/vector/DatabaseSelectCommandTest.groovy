package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Property
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.vector.DatabaseSelectCommand.DatabaseSelectOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The DatabaseSelectCommand Unit Test
 * @author Jared Erickson
 */
class DatabaseSelectCommandTest extends BaseTest {

    @Test
    void executeToShapefile() {
        File inFile = new File("src/test/resources/h2.db")
        File outFile = createTemporaryShapefile("centroids")
        DatabaseSelectCommand cmd = new DatabaseSelectCommand()
        DatabaseSelectOptions options = new DatabaseSelectOptions(
                databaseWorkspace: inFile.absolutePath,
                layerName: "centroids",
                outputWorkspace: outFile.absolutePath,
                sql: "SELECT ST_CENTROID(\"the_geom\") as \"the_geom\", \"id\" from \"polygons\"",
                geometryField: "the_geom|Point"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(outFile)
        assertEquals(10, layer.count)
        assertTrue layer.schema.has("the_geom")
        assertTrue layer.schema.has("id")
        assertEquals layer.schema.geom.typ, "Point"
        layer.eachFeature {Feature f ->
            assertTrue f.geom instanceof Point
        }
    }

    @Test
    void executeToOut() {
        File inFile = new File("src/test/resources/h2.db")
        StringWriter writer = new StringWriter();
        DatabaseSelectCommand cmd = new DatabaseSelectCommand()
        DatabaseSelectOptions options = new DatabaseSelectOptions(
                databaseWorkspace: inFile.absolutePath,
                layerName: "centroids",
                sql: "SELECT ST_CENTROID(\"the_geom\") as \"the_geom\", \"id\" from \"polygons\"",
                geometryField: "the_geom|Point"
        )
        cmd.execute(options, new StringReader(""), writer)
        Layer layer = new CsvReader().read(writer.toString())
        assertEquals(10, layer.count)
        assertTrue layer.schema.has("the_geom")
        assertTrue layer.schema.has("id")
        assertEquals layer.schema.geom.typ, "Point"
        layer.eachFeature {Feature f ->
            assertTrue f.geom instanceof Point
        }
    }


    @Test
    void runToPropertyFile() {
        File inFile = new File("src/test/resources/h2.db")
        File outFile = createTemporaryFile("centroids","properties")
        runApp([
                "vector database select",
                "-w", inFile.absolutePath,
                "-l", "centroids",
                "-o", outFile.absolutePath,
                "-s", "SELECT ST_CENTROID(\"the_geom\") as \"the_geom\", \"id\" from \"polygons\"",
                "-g", "the_geom|Point"
        ],"")
        Layer layer = new Property(outFile)
        assertEquals(10, layer.count)
        assertTrue layer.schema.has("the_geom")
        assertTrue layer.schema.has("id")
        assertEquals layer.schema.geom.typ, "Point"
        layer.eachFeature {Feature f ->
            assertTrue f.geom instanceof Point
        }
    }

    @Test
    void runToOut() {
        File inFile = new File("src/test/resources/h2.db")
        String out = runApp([
                "vector database select",
                "-w", inFile.absolutePath,
                "-l", "centroids",
                "-s", "SELECT ST_CENTROID(\"the_geom\") as \"the_geom\", \"id\" from \"polygons\"",
                "-g", "the_geom|Point"
        ],"")
        Layer layer = new CsvReader().read(out)
        assertEquals(10, layer.count)
        assertTrue layer.schema.has("the_geom")
        assertTrue layer.schema.has("id")
        assertEquals layer.schema.geom.typ, "Point"
        layer.eachFeature {Feature f ->
            assertTrue f.geom instanceof Point
        }
    }

}
