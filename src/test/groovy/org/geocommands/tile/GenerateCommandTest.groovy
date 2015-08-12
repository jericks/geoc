package org.geocommands.tile

import geoscript.layer.*
import org.geocommands.BaseTest
import org.geocommands.tile.GenerateCommand.GenerateOptions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertNotNull

/**
 * The GenerateCommand Unit Test
 * @author Jared Erickson
 */
class GenerateCommandTest extends BaseTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test
    void executeMbtiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFile("earthquakes.mbtiles")
        tileFile.delete()
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: tileFile.absolutePath,
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new MBTiles(tileFile)
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executeGeopackage() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFile("earthquakes.gpkg")
        tileFile.delete()
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: tileFile.absolutePath,
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new GeoPackage(tileFile, "earthquakes")
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executeTMS() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=tms format=png file=${tileFile.absolutePath}",
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new TMS("earthquakes", "png", tileFile, Pyramid.createGlobalMercatorPyramid())
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executeUTFGrid() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")

        Layer layer = new Property(layerFile)
        println layer.count
        println layer.schema.fields

        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=utfgrid file=${tileFile.absolutePath}",
                fields: ["title", "elevation"],
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new UTFGrid(tileFile)
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executeMvtVectorTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=vectortiles format=mvt file=${tileFile.absolutePath}",
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new VectorTiles("earthquakes", tileFile, Pyramid.createGlobalMercatorPyramid(), "mvt")
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executeGeoJsonVectorTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=vectortiles format=geojson file=${tileFile.absolutePath}",
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new VectorTiles("earthquakes", tileFile, Pyramid.createGlobalMercatorPyramid(), "geojson")
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void executePbfVectorTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFolder("earthquakes")
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: "type=vectortiles format=pbf file=${tileFile.absolutePath}",
                baseMap: layerFile,
                startZoom: 0,
                endZoom: 2,
                verbose: false
        )
        cmd.execute(options)

        TileLayer tileLayer = new VectorTiles("earthquakes", tileFile, Pyramid.createGlobalMercatorPyramid(), "pbf")
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

    @Test
    void runMbtiles() {
        File tileFile = temporaryFolder.newFile("earthquakes.mbtiles")
        tileFile.delete()
        File layerFile = getResource("earthquakes.properties")
        runApp([
                "tile generate",
                "-l", tileFile.absolutePath,
                "-m", layerFile,
                "-s", 0,
                "-e", 2,
                "-v", false
        ], "")
        TileLayer tileLayer = new MBTiles(tileFile)
        assertNotNull tileLayer.get(0, 0, 0)
        assertNotNull tileLayer.get(1, 1, 1)
        assertNotNull tileLayer.get(2, 2, 2)
    }

}
