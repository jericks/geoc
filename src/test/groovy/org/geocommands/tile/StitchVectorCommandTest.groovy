package org.geocommands.tile

import geoscript.layer.Layer
import geoscript.workspace.GeoPackage
import geoscript.workspace.Workspace
import org.geocommands.BaseTest
import org.geocommands.tile.GenerateCommand.GenerateOptions
import org.geocommands.tile.StitchVectorCommand.StitchVectorOptions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * The StitchVectorCommand Unit Test
 * @author Jared Erickson
 */
class StitchVectorCommandTest extends BaseTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test
    void executeMvtTiles() {
        File mvtTilesDir = generateMvtTiles()
        File outFile = createTemporaryFile("earthquakes", "gpkg")
        StitchVectorCommand cmd = new StitchVectorCommand()
        StitchVectorOptions options = new StitchVectorOptions(
                tileLayer: "type=vectortiles format=mvt name=earthquakes file=${mvtTilesDir.absolutePath}",
                z: 1,
                outputWorkspace: outFile.absolutePath
        )
        cmd.execute(options)

        Workspace workspace = new GeoPackage(outFile)
        Layer layer = workspace.get("earthquakes")
        assertEquals 47, layer.count
        assertTrue layer.schema.has("title")
    }

    @Test
    void run() {
        File mvtTilesDir = generateMvtTiles()
        File outFile = createTemporaryFile("earthquakes", "gpkg")
        runApp([
                "tile stitch vector",
                "-l", "type=vectortiles name=earthquakes format=mvt file=${mvtTilesDir.absolutePath}",
                "-z", 1,
                "-o", outFile.absolutePath
        ], "")
        Workspace workspace = new GeoPackage(outFile)
        Layer layer = workspace.get("earthquakes")
        assertEquals 47, layer.count
        assertTrue layer.schema.has("title")
    }

    private File generateMvtTiles() {
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateCommand cmd = new GenerateCommand()
        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=vectortiles format=mvt file=${tileFile.absolutePath}",
                fields: ["the_geom", "title"],
                layers: [
                        "layertype=layer file=${layerFile.absolutePath}"
                ],
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)
        tileFile
    }
}
