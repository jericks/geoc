package org.geocommands.geometry

import org.geocommands.geometry.GeoHashDecodeCommand.GeoHashDecodeOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The GeoHashDecodeCommand Unit Test
 * @author Jared Erickson
 */
class GeoHashDecodeCommandTest extends BaseTest {

    @Test void execute() {
        GeoHashDecodeCommand cmd = new GeoHashDecodeCommand()
        GeoHashDecodeOptions options = new GeoHashDecodeOptions(input: "uf8vk6wjr")
        String result = cmd.execute(options)
        assertEquals "POINT (35.00001668930054 60.00000715255737)", result

        options = new GeoHashDecodeOptions(input: "3721965268966289")
        result = cmd.execute(options)
        assertEquals "POINT (34.99999791383743 60.000000447034836)", result
    }

    @Test void run() {
        String result = runApp([
                "geometry geohash decode"
        ], "uf8vk6wjr")
        assertEquals "POINT (35.00001668930054 60.00000715255737)", result.trim()

        result = runApp([
                "geometry geohash decode",
                "-i", "uf8vk6wjr",
        ], "")
        assertEquals "POINT (35.00001668930054 60.00000715255737)", result.trim()
    }
}
