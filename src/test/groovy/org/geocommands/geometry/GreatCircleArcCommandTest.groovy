package org.geocommands.geometry

import org.geocommands.BaseTest
import org.geocommands.geometry.GreatCircleArcCommand.GreatCircleArcCommandOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The GreatCircleArcCommand Unit Test
 * @author Jared Erickson
 */
class GreatCircleArcCommandTest extends BaseTest {

    @Test
    void execute() {
        GreatCircleArcCommandOptions options = new GreatCircleArcCommandOptions(
                startPoint: "POINT (-122 48)",
                endPoint: "POINT (-77 39)"
        )
        GreatCircleArcCommand cmd = new GreatCircleArcCommand()
        String result = cmd.execute(options)
        assertTrue result.startsWith("LINESTRING")
    }

    @Test
    void run() {
        String result = runApp([
                "geometry greatcirclearc",
                "-t", "-77 39",
                "-n", "50"
        ], "-122 48")
        assertTrue result.startsWith("LINESTRING")
    }
}
