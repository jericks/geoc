package org.geocommands

import org.geocommands.ListCommand.ListOptions
import org.junit.Test
import static org.junit.Assert.*

/**
 * The ListCommand Unit Test
 * @author Jared Erickson
 */
class ListCommandTest extends BaseTest {

    @Test void executeToCsv() {
        ListCommand cmd = new ListCommand()
        ListOptions options = new ListOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        String str = w.toString()
        assertTrue str.contains("copy")
        assertTrue str.contains("list")
        assertTrue str.contains("clip")
    }

    @Test void runAsCommandLine() {
        String str = runApp(["list"],"")
        assertTrue str.contains("copy")
        assertTrue str.contains("list")
        assertTrue str.contains("clip")
    }

}
