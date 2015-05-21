package org.geocommands.geometry

import org.geocommands.geometry.GeoHashEncodeCommand.GeoHashEncodeOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The GeoHashEncodeCommand Unit Test
 * @author Jared Erickson
 */
class GeoHashEncodeCommandTest extends BaseTest {

    @Test void execute() {
        GeoHashEncodeCommand cmd = new GeoHashEncodeCommand()
        GeoHashEncodeOptions options = new GeoHashEncodeOptions(input: "POINT (35 60)")
        String result = cmd.execute(options)
        assertEquals "uf8vk6wjr", result

        options = new GeoHashEncodeOptions(input: "POINT (35 60)", type: "string", numberOfChars: 12)
        result = cmd.execute(options)
        assertEquals "uf8vk6wjr4et", result

        options = new GeoHashEncodeOptions(input: "POINT (35 60)", type: "long")
        result = cmd.execute(options)
        assertEquals "3721965268966289", result

        options = new GeoHashEncodeOptions(input: "POINT (35 60)", type: "long", bitDepth: 42)
        result = cmd.execute(options)
        assertEquals "3634731707974", result
    }

    @Test void run() {
        String result = runApp([
                "geometry geohash encode"
        ], "POINT (35 60)")
        assertEquals "uf8vk6wjr", result.trim()

        result = runApp([
                "geometry geohash encode",
                "-i", "POINT (35 60)",
                "-t", "string",
                "-n", "9"
        ], "")
        assertEquals "uf8vk6wjr", result.trim()

        result = runApp([
                "geometry geohash encode",
                "-i", "POINT (35 60)",
                "-t", "long",
                "-d", "52"
        ], "")
        assertEquals "3721965268966289", result.trim()
    }
}
