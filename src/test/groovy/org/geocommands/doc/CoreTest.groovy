package org.geocommands.doc

import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import geoscript.style.io.SimpleStyleReader
import org.geocommands.Command
import org.geocommands.Commands
import org.junit.jupiter.api.Test

class CoreTest extends DocTest {

    @Test
    void createOptionTables() {
        Commands.list().each { Command cmd ->
            writeTextFile("geoc_${cmd.name.replaceAll(' ', '_')}_options", createOptionTable(cmd))
        }
    }

    @Test
    void list() {
        String command = "geoc list"
        String result = runApp(command, "")
        writeTextFile("geoc_list_command", command)
        writeTextFile("geoc_list_command_output", result.split("\n").take(10).join("\n"))
    }

    @Test
    void listWithDescription() {
        String command = "geoc list -d"
        String result = runApp(command, "")
        writeTextFile("geoc_list_description_command", command)
        writeTextFile("geoc_list_description_command_output", result.split("\n").take(10).join("\n"))
    }

    @Test
    void version() {
        String command = "geoc version"
        String result = runApp(command, "")
        writeTextFile("geoc_version_command", command)
        writeTextFile("geoc_version_command_output", result)
    }

    @Test
    void help() {
        String command = "geoc vector buffer --help"
        String result = runApp(command, "")
        writeTextFile("geoc_help_command", command)
        writeTextFile("geoc_help_command_output", result)
    }

    @Test
    void pipe() {
        List commands = ["pipe", "-c",  "vector randompoints -n 10 -g -180,-90,180,90 | vector buffer -d 10"]
        String command = "geoc " + commands.join(" ")
        String result = runApp(commands, "")
        writeTextFile("geoc_pipe_command", command)
        writeTextFile("geoc_pipe_command_output", result.split("\n").take(2).join("\n"))

        Layer layer = new CsvReader().read(result)
        layer.style = new SimpleStyleReader().read("fill=silver fill-opacity=0.5 stroke=#555555 stroke-width=0.5")
        drawOnBasemap("geoc_pipe_command", [layer])
    }
}
