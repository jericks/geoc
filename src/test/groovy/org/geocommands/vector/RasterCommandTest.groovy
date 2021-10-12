package org.geocommands.vector

import geoscript.geom.Point
import geoscript.layer.ArcGrid
import geoscript.layer.GeoTIFF
import geoscript.layer.Raster
import geoscript.proj.Projection
import org.apache.commons.io.input.ReaderInputStream
import org.geocommands.BaseTest
import org.geocommands.vector.RasterCommand.RasterOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The RasterCommand Unit Test
 * @author Jared Erickson
 */
class RasterCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File inFile = getResource("polygons.properties")
        File outFile = createTemporaryFile("raster", "tif")
        RasterCommand cmd = new RasterCommand()
        RasterOptions options = new RasterOptions(
                inputWorkspace: inFile.absolutePath,
                outputRaster: outFile.absolutePath,
                field: "id",
                gridSize: "600,600"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertEquals(0, raster.getValue(new Point(2.56868131868, 2.63461538462)), 0.1)
        assertEquals(1, raster.getValue(new Point(2.15659340659, 7.77747252747)), 0.1)
        assertEquals(2, raster.getValue(new Point(6.27747252747, 4.11813186813)), 0.1)
        assertEquals(3, raster.getValue(new Point(8.53571428571, 9.01373626374)), 0.1)
    }

    @Test
    void executeWithStrings() {
        StringReader reader = getStringReader("polygons.csv")
        StringWriter writer = new StringWriter()

        RasterCommand cmd = new RasterCommand()
        RasterOptions options = new RasterOptions(
                field: "id",
                gridSize: "600,600"
        )
        cmd.execute(options, reader, writer)
        String str = writer.toString()

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(str)))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertEquals(0, raster.getValue(new Point(2.56868131868, 2.63461538462)), 0.1)
        assertEquals(1, raster.getValue(new Point(2.15659340659, 7.77747252747)), 0.1)
        assertEquals(2, raster.getValue(new Point(6.27747252747, 4.11813186813)), 0.1)
        assertEquals(3, raster.getValue(new Point(8.53571428571, 9.01373626374)), 0.1)
    }

    @Test
    void runAsCommandLineWithFiles() {
        File inFile = getResource("polygons.properties")
        File outFile = createTemporaryFile("raster", "tif")
        runApp([
                "vector raster",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath,
                "-d", "id",
                "-s", "600,600"
        ], "")

        GeoTIFF format = new GeoTIFF(outFile)
        Raster raster = format.read()
        assertEquals(0, raster.getValue(new Point(2.56868131868, 2.63461538462)), 0.1)
        assertEquals(1, raster.getValue(new Point(2.15659340659, 7.77747252747)), 0.1)
        assertEquals(2, raster.getValue(new Point(6.27747252747, 4.11813186813)), 0.1)
        assertEquals(3, raster.getValue(new Point(8.53571428571, 9.01373626374)), 0.1)
    }

    @Test
    void runAsCommandLineWithStrings() {
        StringReader reader = getStringReader("polygons.csv")
        String result = runApp([
                "vector raster",
                "-d", "id",
                "-s", "600,600"
        ], reader.text)

        ArcGrid format = new ArcGrid(new ReaderInputStream(new StringReader(result)))
        Raster raster = format.read(new Projection("EPSG:4326"))
        assertEquals(0, raster.getValue(new Point(2.56868131868, 2.63461538462)), 0.1)
        assertEquals(1, raster.getValue(new Point(2.15659340659, 7.77747252747)), 0.1)
        assertEquals(2, raster.getValue(new Point(6.27747252747, 4.11813186813)), 0.1)
        assertEquals(3, raster.getValue(new Point(8.53571428571, 9.01373626374)), 0.1)
    }
}
