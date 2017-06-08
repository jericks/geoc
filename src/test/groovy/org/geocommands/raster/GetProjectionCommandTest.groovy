package org.geocommands.raster

import org.geocommands.raster.GetProjectionCommand.GetProjectionOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals

class GetProjectionCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        GetProjectionCommand command = new GetProjectionCommand()
        GetProjectionOptions options = new GetProjectionOptions(
                inputRaster: inFile
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = "EPSG:4326"
        String actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void executeWithString() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        GetProjectionCommand command = new GetProjectionCommand()
        GetProjectionOptions options = new GetProjectionOptions(
                inputProjection: "EPSG:4326",
                type: "epsg"
        )
        command.execute(options, reader, writer)
        String expected = "4326"
        String actual = writer.toString()
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithFile() {
        File inFile = getResource("alki.tif")
        String actual = runApp([
                "raster projection",
                "-i", inFile.absolutePath,
                "-t", "srs"
        ], "")
        String expected = "EPSG:2927" + NEW_LINE
        assertEquals(expected, actual)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        String actual = runApp([
                "raster projection",
                "-p", "EPSG:4326",
                "-t", "wkt"
        ], reader.text)
        String expected = """GEOGCS["WGS 84", 
  DATUM["World Geodetic System 1984", 
    SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]], 
    AUTHORITY["EPSG","6326"]], 
  PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]], 
  UNIT["degree", 0.017453292519943295], 
  AXIS["Geodetic longitude", EAST], 
  AXIS["Geodetic latitude", NORTH], 
  AUTHORITY["EPSG","4326"]]
"""
        assertEquals(expected.normalize().trim(), actual.normalize().trim())
    }
    
}
