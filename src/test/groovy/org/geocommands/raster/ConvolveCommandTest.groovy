package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.raster.ConvolveCommand.ConvolveOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * The ConvolveCommand Unit Test
 * @author Jared Erickson
 */
class ConvolveCommandTest extends BaseTest {

    @Test void execute() {
        ConvolveCommand cmd = new ConvolveCommand()
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_convolved", "tif")
        ConvolveCommand.ConvolveOptions options = new ConvolveCommand.ConvolveOptions(
                inputRaster: inFile.absolutePath,
                outputRaster: outFile.absolutePath,
                width: 2,
                height: 3
        )
        cmd.execute(options)
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(255.0, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_convolved", "tif")
        runApp([
                "raster convolve",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-w", "2",
                "-h", "3"
        ],"")
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(255.0, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(255.0, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }

}
