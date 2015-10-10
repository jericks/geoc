package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.InfoCommand.InfoOptions
import org.junit.Test

/**
 * The InfoCommand Unit Test
 * @author Jared Erickson
 */
class InfoCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File inFile = getResource("raster.tif")
        InfoCommand command = new InfoCommand()
        InfoOptions options = new InfoOptions(
                inputRaster: inFile
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = """Format: GeoTIFF
Size: 900, 450
Projection ID: EPSG:4326
Projection WKT: GEOGCS["WGS 84",
  DATUM["World Geodetic System 1984",
    SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
    AUTHORITY["EPSG","6326"]],
  PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
  UNIT["degree", 0.017453292519943295],
  AXIS["Geodetic longitude", EAST],
  AXIS["Geodetic latitude", NORTH],
  AUTHORITY["EPSG","4326"]]
Extent: -180.0, -90.00000000000001, 180.0, 90.0
Pixel Size: 0.4, 0.4
Block Size: 900, 9
Bands:
   GRAY_INDEX
      Min Value: 49.0 Max Value: 255.0
"""
        String actual = writer.toString()
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void executeWithString() {
        StringReader reader = getStringReader("raster.asc")
        StringWriter writer = new StringWriter()
        InfoCommand command = new InfoCommand()
        InfoOptions options = new InfoOptions(
                inputProjection: "EPSG:4326"
        )
        command.execute(options, reader, writer)
        String expected = """Format: ArcGrid (ASCII)
Size: 6, 11
Projection ID: EPSG:4326
Projection WKT: GEOGCS["WGS 84",
  DATUM["World Geodetic System 1984",
    SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
    AUTHORITY["EPSG","6326"]],
  PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
  UNIT["degree", 0.017453292519943295],
  AXIS["Geodetic longitude", EAST],
  AXIS["Geodetic latitude", NORTH],
  AUTHORITY["EPSG","4326"]]
Extent: -176.0, 81.6, -173.6, 86.0
Pixel Size: 0.40000000000000097, 0.4000000000000005
Block Size: 6, 11
Bands:
   AsciiGrid
      Min Value: 183.0 Max Value: 187.0
"""
        String actual = writer.toString()
        println actual
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runAsCommandLineWithFile() {
        File inFile = getResource("alki.tif")
        String actual = runApp([
                "raster info",
                "-i", inFile.absolutePath
        ], "")
        String expected = """Format: GeoTIFF
Size: 761, 844
Projection ID: EPSG:2927
Projection WKT: PROJCS["NAD83(HARN) / Washington South (ftUS)",
  GEOGCS["NAD83(HARN)",
    DATUM["NAD83 (High Accuracy Reference Network)",
      SPHEROID["GRS 1980", 6378137.0, 298.257222101, AUTHORITY["EPSG","7019"]],
      TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
      AUTHORITY["EPSG","6152"]],
    PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
    UNIT["degree", 0.017453292519943295],
    AXIS["Geodetic longitude", EAST],
    AXIS["Geodetic latitude", NORTH],
    AUTHORITY["EPSG","4152"]],
  PROJECTION["Lambert Conic Conformal (2SP)", AUTHORITY["EPSG","9802"]],
  PARAMETER["Longitude of natural origin", -120.5],
  PARAMETER["Latitude of false origin", 45.333333333333336],
  PARAMETER["Latitude of 1st standard parallel", 47.33333333333333],
  PARAMETER["False easting", 1640416.667],
  PARAMETER["False northing", 0.0],
  PARAMETER["Scale factor at natural origin", 1.0],
  PARAMETER["Latitude of 2nd standard parallel", 45.833333333333336],
  UNIT["foot_survey_us", 0.30480060960121924],
  AXIS["Easting", EAST],
  AXIS["Northing", NORTH],
  AUTHORITY["EPSG","2927"]]
Extent: 1166191.0260847565, 822960.0090852415, 1167331.8522748263, 824226.3820666744
Pixel Size: 1.4991145730220545, 1.5004419211290778
Block Size: 761, 3
Bands:
   RED_BAND
      Min Value: 10.0 Max Value: 255.0
   GREEN_BAND
      Min Value: 51.0 Max Value: 255.0
   BLUE_BAND
      Min Value: 58.0 Max Value: 250.0
"""
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runAsCommandLineWithString() {
        StringReader reader = getStringReader("raster1.acs")
        String actual = runApp([
                "raster info",
                "-p", "EPSG:4326"

        ], reader.text)
        String expected = """Format: ArcGrid (ASCII)
Size: 7, 5
Projection ID: EPSG:4326
Projection WKT: GEOGCS["WGS 84",
  DATUM["World Geodetic System 1984",
    SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
    AUTHORITY["EPSG","6326"]],
  PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
  UNIT["degree", 0.017453292519943295],
  AXIS["Geodetic longitude", EAST],
  AXIS["Geodetic latitude", NORTH],
  AUTHORITY["EPSG","4326"]]
Extent: 0.0, 0.0, 7.0, 5.0
Pixel Size: 1.0, 1.0
Block Size: 7, 5
Bands:
   AsciiGrid
      Min Value: 5.0 Max Value: 7.0
"""
        assertStringsEqual(expected, actual, true)
    }
}
