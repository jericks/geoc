package org.geocommands.geometry

import org.geocommands.geometry.GeoHashBoundsCommand.GeoHashBoundsOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

/**
 * The GeoHashBoundsCommand Unit Test
 * @author Jared Erickson
 */
class GeoHashBoundsCommandTest extends BaseTest {

    @Test void executeString() {
        GeoHashBoundsCommand cmd = new GeoHashBoundsCommand()
        GeoHashBoundsOptions options = new GeoHashBoundsOptions(
                bounds: "120, 30, 120.0001, 30.0001"
        )
        String result = cmd.execute(options)
        assertStringsEqual("""wtm6dtm6d
wtm6dtm6e
wtm6dtm6s
wtm6dtm6f
wtm6dtm6g
wtm6dtm6u
wtm6dtm74
wtm6dtm75
wtm6dtm7h""", result)
    }

    @Test void executeLong() {
        GeoHashBoundsCommand cmd = new GeoHashBoundsCommand()
        GeoHashBoundsOptions options = new GeoHashBoundsOptions(
                bounds: "120, 30, 120.0001, 30.0001",
                type: "long",
                bitDepth: 45
        )
        String result = cmd.execute(options)
        assertStringsEqual("""28147497671064
28147497671068
28147497671112
28147497671066
28147497671070
28147497671114
28147497671088
28147497671092
28147497671136""", result)
    }

    @Test void runString() {
        String result = runApp([
                "geometry geohash bounds"
        ], "120, 30, 120.0001, 30.0001")
        assertStringsEqual("""wtm6dtm6d
wtm6dtm6e
wtm6dtm6s
wtm6dtm6f
wtm6dtm6g
wtm6dtm6u
wtm6dtm74
wtm6dtm75
wtm6dtm7h""", result)
    }

    @Test void runLong() {
        String result = runApp([
                "geometry geohash bounds",
                "-b","120, 30, 120.0001, 30.0001",
                "-t","long",
                "-d","45"
        ],"")
        assertStringsEqual("""28147497671064
28147497671068
28147497671112
28147497671066
28147497671070
28147497671114
28147497671088
28147497671092
28147497671136""", result)
    }

}
