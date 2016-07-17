package org.geocommands.raster

import geoscript.geom.Point
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import org.geocommands.raster.NormalizeCommand.NormalizeOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * The NormalizeCommand Unit Test.
 * @author Jared Erickson
 */
class NormalizeCommandTest extends BaseTest {

    @Test void execute() {
        NormalizeCommand cmd = new NormalizeCommand()
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_norm", "tif")
        NormalizeOptions options = new NormalizeOptions(
                inputRaster: inFile.absolutePath,
                outputRaster: outFile.absolutePath
        )
        cmd.execute(options)
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(1.0, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }

    @Test void run() {
        File inFile = getResource("raster.tif")
        File outFile = createTemporaryFile("raster_norm", "tif")
        runApp([
                "raster normalize",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ],"")
        Raster outRaster = new GeoTIFF(outFile).read()
        assertEquals(1.0, outRaster.getValue(new Point(-120, 47), 0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(120, 45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(0, -45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(0, 45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(40, -45),  0), 0.1)
        assertEquals(1.0, outRaster.getValue(new Point(40, 45),  0), 0.1)
    }
    
}
