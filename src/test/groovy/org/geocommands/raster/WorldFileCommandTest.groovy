package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.WorldFileCommand.WorldFileOptions
import org.junit.jupiter.api.Test

/**
 * The WorldFileCommand Unit Test
 * @author Jared Erickson
 */
class WorldFileCommandTest extends BaseTest {

    @Test
    void executeWithFile() {
        File outFile = createTemporaryFile("worldfile", "txt")
        WorldFileCommand command = new WorldFileCommand()
        WorldFileOptions options = new WorldFileOptions(
                bounds: "10 11 20 21",
                size: "800,751",
                file: outFile
        )
        command.execute(options, new StringReader(""), new StringWriter())

        String expected = """0.0125
0.0
0.0
-0.013315579227696404
10.00625
20.993342210386153
"""
        String actual = outFile.text
        assertStringsEqual(expected, actual)
    }

    @Test
    void executeToWriter() {
        WorldFileCommand command = new WorldFileCommand()
        WorldFileOptions options = new WorldFileOptions(
                bounds: "10 11 20 21",
                size: "800,751"
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)

        String expected = """0.0125
0.0
0.0
-0.013315579227696404
10.00625
20.993342210386153
"""
        String actual = writer.toString()
        assertStringsEqual(expected, actual)
    }

    @Test
    void runAsCommandLineWithFile() {
        File outFile = createTemporaryFile("worldfile", "txt")
        runApp([
                "raster worldfile",
                "-b", "10 11 20 21",
                "-s", "800,751",
                "-f", outFile.absolutePath
        ], "")

        String expected = """0.0125
0.0
0.0
-0.013315579227696404
10.00625
20.993342210386153
"""
        String actual = outFile.text
        println actual
        assertStringsEqual(expected, actual)
    }

    @Test
    void runAsCommandLine() {
        String actual = runApp([
                "raster worldfile",
                "-b", "10 11 20 21",
                "-s", "800,751"
        ], "")

        String expected = """0.0125
0.0
0.0
-0.013315579227696404
10.00625
20.993342210386153
"""
        assertStringsEqual(expected, actual)
    }

}
