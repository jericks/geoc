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
                tileLayer: mvtTilesDir.absolutePath,
                tileLayerName: "earthquakes",
                type: "mvt",
                z: 1,
                outputWorkspace: outFile.absolutePath
        )
        cmd.execute(options)

        Workspace workspace = new GeoPackage(outFile)
        Layer layer = workspace.get("earthquakes")
        assertEquals 47, layer.count
        assertTrue layer.schema.has("title")
        assertTrue layer.schema.has("elevation")
    }

    @Test
    void run() {
        File mvtTilesDir = generateMvtTiles()
        File outFile = createTemporaryFile("earthquakes", "gpkg")
        runApp([
                "tile stitch vector",
                "-l", mvtTilesDir.absolutePath,
                "-n", "earthquakes",
                "-t", "mvt",
                "-z", 1,
                "-o", outFile.absolutePath
        ], "")
        Workspace workspace = new GeoPackage(outFile)
        Layer layer = workspace.get("earthquakes")
        assertEquals 47, layer.count
        assertTrue layer.schema.has("title")
        assertTrue layer.schema.has("elevation")
    }

    private File generateMvtTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: tileFile.absolutePath,
                tileLayerName: "earthquakes",
                type: "mvt",
                fields: ["the_geom", "title", "elevation"],
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)
        tileFile
    }
}
