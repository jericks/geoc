package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.CreateCommand.CreateOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The CreateCommand Unit Test
 * @author Jared Erickson
 */
class CreateCommandTest extends BaseTest {

    @Test void execute() {
        File outFile = createTemporaryShapefile("points")
        CreateCommand cmd = new CreateCommand()
        CreateOptions options = new CreateOptions(
            fields: [
                "the_geom": "POINT EPSG:4326",
                "id": "int",
                "name": "string"
            ],
            outputWorkspace: outFile.absolutePath
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(outFile)
        assertTrue shp.schema.has("the_geom")
        assertEquals "Point", shp.schema.get("the_geom").typ
        assertEquals "EPSG:4326", shp.schema.get("the_geom").proj.id
        assertTrue shp.schema.has("id")
        assertEquals "Integer", shp.schema.get("id").typ
        assertTrue shp.schema.has("name")
        assertEquals "String", shp.schema.get("name").typ
    }

    @Test void executeToCsv() {
        CreateCommand cmd = new CreateCommand()
        CreateOptions options = new CreateOptions(
            fields: [
                "the_geom": "POINT EPSG:4326",
                "id": "int",
                "name": "string"
            ]
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals "csv the_geom: Point, id: String, name: String", layer.schema.toString()
    }

    @Test void runAsCommandLine() {
        File shpFile = createTemporaryShapefile("points")
        App.main([
            "vector create",
            "-f", "the_geom=POINT EPSG:4326",
            "-f", "id=int",
            "-f", "name=String",
            "-o", shpFile.absolutePath
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertTrue shp.schema.has("the_geom")
        assertEquals "Point", shp.schema.get("the_geom").typ
        assertEquals "EPSG:4326", shp.schema.get("the_geom").proj.id
        assertTrue shp.schema.has("id")
        assertEquals "Integer", shp.schema.get("id").typ
        assertTrue shp.schema.has("name")
        assertEquals "String", shp.schema.get("name").typ
    }
}