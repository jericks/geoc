package org.geocommands.raster

import geoscript.geom.Bounds
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.BaseTest
import org.geocommands.raster.MosaicCommand.MosaicOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

/**
 * The CropCommand Unit Test
 * @author Jared Erickson
 */
class MosaicCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        List<String> inFiles = (0..5).collect { int i -> getResource("mosaic/alki${i}.tif").getAbsolutePath() }
        File outFile = createTemporaryFile("alki_mosaic", ".tif")
        MosaicCommand command = new MosaicCommand()
        MosaicOptions options = new MosaicOptions(
                rasters: inFiles,
                outputRaster: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File outFile = createTemporaryFile("alki_mosaic", ".tif")
        runApp([
                "raster mosaic",
                "-r", getResource("mosaic/alki0.tif").absolutePath,
                "-r", getResource("mosaic/alki1.tif").absolutePath,
                "-r", getResource("mosaic/alki2.tif").absolutePath,
                "-r", getResource("mosaic/alki3.tif").absolutePath,
                "-r", getResource("mosaic/alki4.tif").absolutePath,
                "-r", getResource("mosaic/alki5.tif").absolutePath,
                "-o", outFile.absolutePath
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
    }

}
