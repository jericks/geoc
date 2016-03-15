package org.geocommands.tile

import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.tile.GenerateCommand.GenerateOptions
import org.geocommands.tile.StitchRasterCommand.StitchRasterOptions
import org.geocommands.BaseTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import static org.junit.Assert.*

/**
 * The StitchRasterCommand Unit Test
 * @author Jared Erickson
 */
class StitchRasterCommandTest extends BaseTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test void executeMBTiles() {
        File mbtilesFile = generateMBTiles()
        File rasterFile = createTemporaryFile("earthquakes","tif")
        StitchRasterCommand cmd = new StitchRasterCommand()
        StitchRasterOptions options = new StitchRasterOptions(
                tileLayer: mbtilesFile.absolutePath,
                z: 1,
                outputRaster: rasterFile.absolutePath
        )
        cmd.execute(options)

        GeoTIFF geotiff = new GeoTIFF(rasterFile)
        Raster raster = geotiff.read()
        assertNotNull raster
        assertNotNull raster.bounds
    }

    @Test void runMBTiles() {
        File mbtilesFile = generateMBTiles()
        File rasterFile = createTemporaryFile("earthquakes","tif")
        runApp([
           "tile stitch raster",
                "-l", mbtilesFile.absolutePath,
                "-z", 1,
                "-o", rasterFile
        ],"")

        GeoTIFF geotiff = new GeoTIFF(rasterFile)
        Raster raster = geotiff.read()
        assertNotNull raster
        assertNotNull raster.bounds
    }

    private File generateMBTiles() {
        GenerateCommand cmd = new GenerateCommand()
        File tileFile = temporaryFolder.newFile("earthquakes.mbtiles")
        tileFile.delete()
        File layerFile = getResource("earthquakes.properties")
        GenerateOptions options = new GenerateOptions(
                tileLayer: tileFile.absolutePath,
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
