package org.geocommands

import org.geocommands.ListCommand.ListOptions
import org.junit.Test

import static org.junit.Assert.*

/**
 * The ListCommand Unit Test
 * @author Jared Erickson
 */
class ListCommandTest extends BaseTest {

    @Test
    void execute() {
        ListCommand cmd = new ListCommand()
        ListOptions options = new ListOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        String str = w.toString()
        assertTrue str.contains("vector copy")
        assertTrue str.contains("list")
        assertTrue str.contains("vector clip")
    }

    @Test
    void executeWithDescription() {
        ListCommand cmd = new ListCommand()
        ListOptions options = new ListOptions(showDescriptions: true)
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        String str = w.toString()
        assertTrue str.contains("vector copy = Copy the input Layer to the output Layer")
        assertTrue str.contains("list = List all geocommands")
        assertTrue str.contains("vector clip = Clip the input Layer by the other Layer to produce the output Layer")
    }

    @Test
    void runAsCommandLine() {
        String str = runApp(["list"], "")
        assertTrue str.contains("vector copy")
        assertTrue str.contains("list")
        assertTrue str.contains("vector clip")
    }

    @Test
    void runAsCommandLineWithDescription() {
        String str = runApp(["list", "-d"], "")
        assertTrue str.contains("vector copy = Copy the input Layer to the output Layer")
        assertTrue str.contains("list = List all geocommands")
        assertTrue str.contains("vector clip = Clip the input Layer by the other Layer to produce the output Layer")
    }
    
}
