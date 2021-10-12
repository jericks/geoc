package org.geocommands

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class CommandsTest {

    @Test
    void list() {
        List<Command> cmds = Commands.list()
        assertTrue(!cmds.isEmpty())
        assertNotNull(cmds.find { it.name.equalsIgnoreCase("list") })
    }

    @Test
    void find() {
        assertNotNull(Commands.find("list"))
        assertNull(Commands.find("asdf"))
    }

    @Test
    void execute() {
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        Commands.execute("list", [:], reader, writer)
        String result = writer.toString()
        assertTrue result.contains("vector copy")
        assertTrue result.contains("list")
        assertTrue result.contains("vector clip")
    }

    @Test
    void executeToString() {
        String result = Commands.execute("list", [showDescriptions: true])
        assertTrue result.contains("vector copy = Copy the input Layer to the output Layer")
        assertTrue result.contains("list = List all geocommands")
        assertTrue result.contains("vector clip = Clip the input Layer by the other Layer to produce the output Layer")
    }
}
