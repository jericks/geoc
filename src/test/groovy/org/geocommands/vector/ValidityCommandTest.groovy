package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.ValidityCommand.ValidityOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The ValidityCommand Unit Test
 * @author Jared Erickson
 */
class ValidityCommandTest extends BaseTest {

    private static String NEW_LINE = System.getProperty("line.separator")

    @Test
    void executeValid() {
        ValidityCommand cmd = new ValidityCommand()
        File file = getResource("polygons.properties")
        ValidityOptions options = new ValidityOptions(
                inputWorkspace: file.absolutePath,
                fields: ["id"]
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals "", w.toString()
    }

    @Test
    void executeInvalid() {
        ValidityCommand cmd = new ValidityCommand()
        File file = getResource("invalid.properties")
        ValidityOptions options = new ValidityOptions(
                inputWorkspace: file.absolutePath,
                fields: ["id", "name"]
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        assertEquals "1,Polygon 1,Self-intersection" + NEW_LINE, w.toString()
    }

    @Test
    void executeWithCsv() {
        ValidityCommand cmd = new ValidityCommand()
        ValidityOptions options = new ValidityOptions(fields: ["id", "name"])
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("invalid.csv"), w)
        assertEquals "1,Polygon 1,Self-intersection" + NEW_LINE, w.toString()
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("invalid.properties")
        String output = runApp(["vector validity", "-i", file.absolutePath, "-f", "id", "-f", "name"], "")
        assertEquals "1,Polygon 1,Self-intersection" + NEW_LINE, output

        output = runApp(["vector validity", "-f", "id", "-f", "name"], readCsv("invalid.csv").text)
        assertEquals "1,Polygon 1,Self-intersection" + NEW_LINE, output
    }
}
