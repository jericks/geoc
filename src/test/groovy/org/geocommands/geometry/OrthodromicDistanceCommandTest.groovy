package org.geocommands.geometry

import org.geocommands.BaseTest
import org.geocommands.geometry.OrthodromicDistanceCommand.OrthodromicDistanceOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The OrthodromicDistanceCommand Unit Test
 * @author Jared Erickson
 */
class OrthodromicDistanceCommandTest extends BaseTest {

    @Test
    void execute() {
        OrthodromicDistanceCommand cmd = new OrthodromicDistanceCommand()
        OrthodromicDistanceOptions options = new OrthodromicDistanceOptions(
                ellipsoid: "wgs84",
                point1: "-86.67 36.12",
                point2: "-118.40 33.94"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        assertEquals 2892776.9573561614, writer.toString() as Double, 0.01
    }

    @Test
    void executeWithReader() {
        OrthodromicDistanceCommand cmd = new OrthodromicDistanceCommand()
        OrthodromicDistanceOptions options = new OrthodromicDistanceOptions(
                ellipsoid: "wgs84",
                point2: "-118.40 33.94"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader("-86.67 36.12"), writer)
        assertEquals 2892776.9573561614, writer.toString() as Double, 0.01
    }

    @Test
    void runAsCommandLine() {
        String output = runApp([
                "geometry orthodromicdistance",
                "-e", "wgs84",
                "-p", "-86.67 36.12",
                "-t", "-118.40 33.94"
        ], "")
        assertEquals 2892776.9573561614, output as Double, 0.01
    }

    @Test
    void runAsCommandLineWithReader() {
        String output = runApp([
                "geometry orthodromicdistance",
                "-e", "wgs84",
                "-t", "-118.40 33.94"
        ], "POINT (-86.67 36.12)")
        assertEquals 2892776.9573561614, output as Double, 0.01
    }
}
