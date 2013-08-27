package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.raster.ScaleCommand.ScaleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * The ScaleCommand Unit Test
 * @author Jared Erickson
 */
class ScaleCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("alki.tif")
        File outFile = createTemporaryFile("alki", "tif")
        println outFile
        ScaleCommand command = new ScaleCommand()
        ScaleOptions options = new ScaleOptions(
            inputRaster: inFile,
            outputRaster: outFile,
            x: 2,
            y: 3,
            xTrans: 0,
            yTrans: 0,
            interpolation: "nearest"
        )
        command.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF()
        Raster inRaster = format.read(inFile)
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        assertTrue(inRaster.pixelSize[0] > outRaster.pixelSize[0])
        assertTrue(inRaster.pixelSize[1] > outRaster.pixelSize[1])
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        ScaleCommand command = new ScaleCommand()
        ScaleOptions options = new ScaleOptions(
                x: 2,
                y: 3,
                xTrans: 0,
                yTrans: 0,
                interpolation: "nearest"
        )
        command.execute(options, reader, writer)

        ArcGrid format = new ArcGrid()
        Raster inRaster = format.read(new ReaderInputStream(getStringReader("raster.asc")))
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(writer.toString())))
        assertNotNull(outRaster)
        assertTrue(inRaster.pixelSize[0] > outRaster.pixelSize[0])
        assertTrue(inRaster.pixelSize[1] > outRaster.pixelSize[1])
    }

    // geoc raster scale -i raster.tif -x 2 -y 3 -o raster_scaled.tif
    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_scaled", ".tif")
        runApp([
                "raster scale",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-x", "2",
                "-y", "3",
                "-t", "0",
                "-r", "0",
                "-l","nearest"
        ], "")

        GeoTIFF format = new GeoTIFF()
        Raster inRaster = format.read(inFile)
        Raster outRaster = format.read(outFile)
        assertNotNull(outRaster)
        assertTrue(inRaster.pixelSize[0] > outRaster.pixelSize[0])
        assertTrue(inRaster.pixelSize[1] > outRaster.pixelSize[1])
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        String result = runApp([
                "raster scale",
                "-x", "2",
                "-y", "3",
                "-t", "0",
                "-r", "0",
                "-l","nearest"
        ], reader.text)

        ArcGrid format = new ArcGrid()
        Raster inRaster = format.read(new ReaderInputStream(getStringReader("raster1.acs")))
        Raster outRaster = format.read(new ReaderInputStream(new StringReader(result)))
        assertNotNull(outRaster)
        assertTrue(inRaster.pixelSize[0] > outRaster.pixelSize[0])
        assertTrue(inRaster.pixelSize[1] > outRaster.pixelSize[1])
    }

}