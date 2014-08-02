package org.geocommands.proj

import org.geocommands.BaseTest
import org.geocommands.proj.WktCommand.WktOptions
import org.junit.Test

/**
 * The PrjCommand Unit Test
 * @author Jared Erickson
 */
class WktCommandTest extends BaseTest {

    @Test
    void executeToString() {
        WktCommand command = new WktCommand()
        WktOptions options = new WktOptions(
                epsg: "EPSG:4326"
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
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
        String actual = writer.toString()
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void executeToFile() {
        File file = createTemporaryFile("layer", "prj")
        WktCommand command = new WktCommand()
        WktOptions options = new WktOptions(
                epsg: "EPSG:4326",
                file: file
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
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
        String actual = file.text
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runAsCommandLineToString() {
        String actual = runApp([
                "proj wkt",
                "-e", "EPSG:4326"
        ], "")
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
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runAsCommandLineToFile() {
        File file = createTemporaryFile("layer", "prj")
        runApp([
                "proj wkt",
                "-e", "EPSG:4326",
                "-f", file.absolutePath
        ], "")
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
        String actual = file.text
        assertStringsEqual(expected, actual, true)
    }
}
