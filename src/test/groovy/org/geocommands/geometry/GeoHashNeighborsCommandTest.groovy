package org.geocommands.geometry

import org.geocommands.geometry.GeoHashNeighborsCommand.GeoHashNeighborsOptions
import org.geocommands.BaseTest
import org.junit.Test

/**
 * The GeoHashNeighborsCommand Unit Test.
 * @author Jared Erickson
 */
class GeoHashNeighborsCommandTest extends BaseTest {

    @Test void executeString() {
        GeoHashNeighborsCommand cmd = new GeoHashNeighborsCommand()
        GeoHashNeighborsOptions options = new GeoHashNeighborsOptions(
                input: "uf8vk6wjr"
        )
        String result = cmd.execute(options)
        assertStringsEqual result, """NORTH,uf8vk6wjx
NORTHEAST,uf8vk6wm8
EAST,uf8vk6wm2
SOUTHEAST,uf8vk6wm0
SOUTH,uf8vk6wjp
SOUTHWEST,uf8vk6wjn
WEST,uf8vk6wjq
NORTHWEST,uf8vk6wjw"""
    }

    @Test void executeLong() {
        GeoHashNeighborsCommand cmd = new GeoHashNeighborsCommand()
        GeoHashNeighborsOptions options = new GeoHashNeighborsOptions(
                input: "372196526"
        )
        String result = cmd.execute(options)
        assertStringsEqual result, """NORTH,372196527
NORTHEAST,372196869
EAST,372196868
SOUTHEAST,372196865
SOUTH,372196523
SOUTHWEST,372196521
WEST,372196524
NORTHWEST,372196525"""
    }

    @Test void runString() {
        String result = runApp([
            "geometry geohash neighbors"
        ], "uf8vk6wjr")
        assertStringsEqual result, """NORTH,uf8vk6wjx
NORTHEAST,uf8vk6wm8
EAST,uf8vk6wm2
SOUTHEAST,uf8vk6wm0
SOUTH,uf8vk6wjp
SOUTHWEST,uf8vk6wjn
WEST,uf8vk6wjq
NORTHWEST,uf8vk6wjw
"""
    }

    @Test void runLong() {
        String result = runApp([
                "geometry geohash neighbors"
        ], "372196526")
        assertStringsEqual result, """NORTH,372196527
NORTHEAST,372196869
EAST,372196868
SOUTHEAST,372196865
SOUTH,372196523
SOUTHWEST,372196521
WEST,372196524
NORTHWEST,372196525
"""
    }

}
