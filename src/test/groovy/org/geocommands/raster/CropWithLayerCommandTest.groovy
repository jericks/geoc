package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.CropWithLayerCommand.CropWithLayerOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The CropWithLayerCommand Unit Test
 * @author Jared Erickson
 */
class CropWithLayerCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        File layerFile = getResource("crop_polys.shp")
        CropWithLayerCommand command = new CropWithLayerCommand()
        CropWithLayerOptions options = new CropWithLayerOptions(
                inputRaster: inFile,
                outputRaster: outFile,
                inputWorkspace: layerFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster raster = format.read(outFile)
        assertNotNull(raster)
        // No Data
        [
                [1166759.13176, 823744.027034],
                [1166588.38236, 823469.905029],
                [1166953.87837, 823903.700795],
                [1166388.09793, 823748.641883],
                [1167067.40364, 823704.339336]
        ].each { pt ->
            assertEquals(0.0, raster.getValue(new Point(pt[0], pt[1])), 0.1)
        }
        // Data
        [
                [1166471.1652, 823936.927705],
                [1167120.93588, 823842.784794],
                [1166430.55454, 823474.519877],
                [1167101.55352, 823581.584364]
        ].each { pt ->
            assertTrue(raster.getValue(new Point(pt[0], pt[1])) > 0)
        }
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        File layerFile = getResource("raster_rects.shp")
        CropWithLayerCommand command = new CropWithLayerCommand()
        CropWithLayerOptions options = new CropWithLayerOptions(
                inputWorkspace: layerFile
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster raster = format.read(new ReaderInputStream(new StringReader(writer.toString())))
        assertNotNull(raster)

        // No Data
        [
                [-174.634557329, 85.4595791001],
                [-174.265761974, 83.2132801161],
                [-175.96892598, 82.8444847605]
        ].each { pt ->
            assertEquals(-9999.0, raster.getValue(new Point(pt[0], pt[1])), 0.1)
        }
        // Data
        [
                [-175.754354136, 85.3925253991],
                [-174.507155298, 84.2727285922],
                [-174.66808418, 82.5762699565]
        ].each { pt ->
            assertTrue(raster.getValue(new Point(pt[0], pt[1])) > 0)
        }
    }

    @Test
    void runWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        File layerFile = getResource("raster_rects.shp")
        String result = runApp([
                "raster crop with layer",
                "-w", layerFile.absolutePath
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster raster = format.read(new ReaderInputStream(new StringReader(result)))
        assertNotNull(raster)

        // No Data
        [
                [-174.634557329, 85.4595791001],
                [-174.265761974, 83.2132801161],
                [-175.96892598, 82.8444847605]
        ].each { pt ->
            assertEquals(-9999.0, raster.getValue(new Point(pt[0], pt[1])), 0.1)
        }
        // Data
        [
                [-175.754354136, 85.3925253991],
                [-174.507155298, 84.2727285922],
                [-174.66808418, 82.5762699565]
        ].each { pt ->
            assertTrue(raster.getValue(new Point(pt[0], pt[1])) > 0)
        }
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki_cropped", ".tif")
        File layerFile = getResource("crop_polys.shp")
        runApp([
                "raster crop with layer",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-w", layerFile.absolutePath
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster raster = format.read(outFile)
        assertNotNull(raster)
        // No Data
        [
                [1166759.13176, 823744.027034],
                [1166588.38236, 823469.905029],
                [1166953.87837, 823903.700795],
                [1166388.09793, 823748.641883],
                [1167067.40364, 823704.339336]
        ].each { pt ->
            assertEquals(0.0, raster.getValue(new Point(pt[0], pt[1])), 0.1)
        }
        // Data
        [
                [1166471.1652, 823936.927705],
                [1167120.93588, 823842.784794],
                [1166430.55454, 823474.519877],
                [1167101.55352, 823581.584364]
        ].each { pt ->
            assertTrue(raster.getValue(new Point(pt[0], pt[1])) > 0)
        }
    }
}
