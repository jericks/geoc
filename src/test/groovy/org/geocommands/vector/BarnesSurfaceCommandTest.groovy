package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.vector.BarnesSurfaceCommand.BarnesSurfaceOptions
import org.geocommands.BaseTest
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The BarnesSurfaceCommand Unit Test
 */
class BarnesSurfaceCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("point_grid.properties")
        File outFile = createTemporaryFile("barnes", "tif")
        BarnesSurfaceCommand cmd = new BarnesSurfaceCommand()
        BarnesSurfaceOptions options = new BarnesSurfaceOptions(
                inputWorkspace: inFile.absolutePath,
                outputRaster: outFile.absolutePath,
                valueAttr: "value",
                scale: 1,
                minObservations: 1,
                width: 800,
                height: 800
        )
        cmd.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals(11.1973, raster.getValue(new Point(0.970705521472, 1.00935582822)), 0.1)
        assertEquals(21.3022, raster.getValue(new Point(3.03849693252, 4.04340490798)), 0.1)
        assertEquals(30.1299, raster.getValue(new Point(5.64739263804, 6.26579754601)), 0.1)
        assertEquals(39.945, raster.getValue(new Point(8.73941717791, 8.89401840491)), 0.1)
    }

    @Test
    void executeWithString() {
        StringReader reader = getStringReader("point_grid.csv")
        StringWriter writer = new StringWriter()
        BarnesSurfaceCommand cmd = new BarnesSurfaceCommand()
        BarnesSurfaceOptions options = new BarnesSurfaceOptions(
                valueAttr: "value",
                scale: 1,
                minObservations: 1,
                width: 100,
                height: 100
        )
        cmd.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertNotNull(raster)
        assertEquals(11.1973, raster.getValue(new Point(0.970705521472, 1.00935582822)), 0.1)
        assertEquals(21.3022, raster.getValue(new Point(3.03849693252, 4.04340490798)), 0.1)
        assertEquals(30.1299, raster.getValue(new Point(5.64739263804, 6.26579754601)), 0.1)
        assertEquals(39.945, raster.getValue(new Point(8.73941717791, 8.89401840491)), 0.1)
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("point_grid.properties")
        File outFile = createTemporaryFile("barnes", "tif")
        runApp([
            "vector barnessurface",
            "-i", inFile.absolutePath,
            "-o", outFile.absolutePath,
            "-s", "1",
            "-v", "value",
            "-m", "1",
            "-w", "800",
            "-h", "800"
        ],"")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals(11.1973, raster.getValue(new Point(0.970705521472, 1.00935582822)), 0.1)
        assertEquals(21.3022, raster.getValue(new Point(3.03849693252, 4.04340490798)), 0.1)
        assertEquals(30.1299, raster.getValue(new Point(5.64739263804, 6.26579754601)), 0.1)
        assertEquals(39.945, raster.getValue(new Point(8.73941717791, 8.89401840491)), 0.1)
    }

    @Test
    void runWithString() {
        StringReader reader = getStringReader("point_grid.csv")
        String result = runApp([
                "vector barnessurface",
                "-s", "1",
                "-v", "value",
                "-m", "1",
                "-w", "100",
                "-h", "100"
        ],reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertNotNull(raster)
        assertEquals(11.1973, raster.getValue(new Point(0.970705521472, 1.00935582822)), 0.1)
        assertEquals(21.3022, raster.getValue(new Point(3.03849693252, 4.04340490798)), 0.1)
        assertEquals(30.1299, raster.getValue(new Point(5.64739263804, 6.26579754601)), 0.1)
        assertEquals(39.945, raster.getValue(new Point(8.73941717791, 8.89401840491)), 0.1)
    }
}
