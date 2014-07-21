package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.vector.HeatmapCommand.HeatmapOptions
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * The HeatmapCommand Unit Test
 * @author Jared Erickson
 */
class HeatmapCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("earthquakes.properties")
        File outFile = createTemporaryFile("raster", "tif")
        HeatmapCommand cmd = new HeatmapCommand()
        HeatmapOptions options = new HeatmapOptions(
                inputWorkspace: inFile.absolutePath,
                outputRaster: outFile.absolutePath,
                radiusPixels: 50,
                width: 800,
                height: 600
        )
        cmd.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals(0, raster.getValue(new Point(-26.4385759658, 38.7692617101)), 0.1)
        assertEquals(0.821675, raster.getValue(new Point(-119.279641936, 39.2684072261)), 0.1)
        assertEquals(0.243227, raster.getValue(new Point(-153.720682537, 59.7333733807)), 0.1)
        assertEquals(0.796126, raster.getValue(new Point(138.778589819, 38.7692617101)), 0.1)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("earthquakes.csv")
        StringWriter writer = new StringWriter()
        HeatmapCommand cmd = new HeatmapCommand()
        HeatmapOptions options = new HeatmapOptions(
                radiusPixels: 50,
                width: 200,
                height: 200
        )
        cmd.execute(options, reader, writer)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(writer.toString())))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertNotNull(raster)
        assertEquals(0.81143, raster.getValue(new Point(-121.711567991, 208.78804204)), 0.1)
        assertEquals(0, raster.getValue(new Point(46.6164178681, 247.579676641)), 0.1)
        assertEquals(0.293128, raster.getValue(new Point(-71.1439014571, 133.28289612)), 0.1)
        assertEquals(0.500028, raster.getValue(new Point(133.897595721, 233.725521426)), 0.1)
    }

    @Test
    void runWithFiles() {
        File inFile = getResource("earthquakes.properties")
        File outFile = createTemporaryFile("raster", "tif")
        runApp([
                "vector heatmap",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-r", 50,
                "-w", 800,
                "-h", 600
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertNotNull(raster)
        assertEquals(0, raster.getValue(new Point(-26.4385759658, 38.7692617101)), 0.1)
        assertEquals(0.821675, raster.getValue(new Point(-119.279641936, 39.2684072261)), 0.1)
        assertEquals(0.243227, raster.getValue(new Point(-153.720682537, 59.7333733807)), 0.1)
        assertEquals(0.796126, raster.getValue(new Point(138.778589819, 38.7692617101)), 0.1)
    }

    @Test
    void runWithStrings() {
        StringReader reader = getStringReader("earthquakes.csv")
        String result = runApp([
                "vector heatmap",
                "-r", 50,
                "-w", 200,
                "-h", 200
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertNotNull(raster)
        assertEquals(0.81143, raster.getValue(new Point(-121.711567991, 208.78804204)), 0.1)
        assertEquals(0, raster.getValue(new Point(46.6164178681, 247.579676641)), 0.1)
        assertEquals(0.293128, raster.getValue(new Point(-71.1439014571, 133.28289612)), 0.1)
        assertEquals(0.500028, raster.getValue(new Point(133.897595721, 233.725521426)), 0.1)
    }

}
