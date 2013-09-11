package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.UniqueValuesCommand.UniqueValuesOptions
import org.junit.Test

/**
 * The UniqueValuesCommand Test
 * @author Jared Erickson
 */
class UniqueValuesCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("polygons.properties")
        StringWriter writer = new StringWriter()
        UniqueValuesCommand cmd = new UniqueValuesCommand()
        UniqueValuesOptions options = new UniqueValuesOptions(
                inputWorkspace: file.absolutePath,
                field: "col"
        )
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = """0
1
"""
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("polygons.csv")
        StringWriter writer = new StringWriter()
        UniqueValuesCommand cmd = new UniqueValuesCommand()
        UniqueValuesOptions options = new UniqueValuesOptions(
                field: "row"
        )
        cmd.execute(options, reader, writer)
        String actual = writer.toString()
        String expected = """1
0
"""
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runWithFiles() {
        File file = getResource("polygons.properties")
        String actual = runApp([
                "vector uniquevalues",
                "-i", file.absolutePath,
                "-f", "col"
        ], "")
        String expected = """0
1
"""
        assertStringsEqual(expected, actual, true)
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("polygons.csv")
        String actual = runApp([
                "vector uniquevalues",
                "-f", "col"
        ], reader.text)
        String expected = """1
0
"""
        assertStringsEqual(expected, actual, true)
    }
}
