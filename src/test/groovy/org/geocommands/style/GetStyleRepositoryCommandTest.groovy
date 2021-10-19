package org.geocommands.style

import geoscript.filter.Color
import geoscript.style.Fill
import geoscript.style.io.SLDWriter
import groovy.sql.Sql
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class GetStyleRepositoryCommandTest extends BaseTest {

    @Test
    void directory() {
        File styleDirectory = createDir("styles")
        save("directory", [file: styleDirectory], "states", "states")
        save("directory", [file: styleDirectory], "states", "states-blue")

        GetStyleRepositoryCommand cmd = new GetStyleRepositoryCommand()
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(new GetStyleRepositoryCommand.GetStyleRepositoryOptions(
                type: "directory",
                params: [file: styleDirectory],
                layerName: "states",
                styleName: "states-blue"
        ), reader, writer)
        String sld = writer.toString()
        assertNotNull(sld)
        assertTrue(sld.contains("<sld:UserLayer>"))
    }

    @Test
    void nestedDirectory() {
        File styleDirectory = createDir("styles")
        save("nested-directory", [file: styleDirectory], "states", "states")
        save("nested-directory", [file: styleDirectory], "states", "states-blue")
        File statesStyleDirectory = new File(styleDirectory, "states")

        assertTrue(statesStyleDirectory.list().length == 2)
        GetStyleRepositoryCommand cmd = new GetStyleRepositoryCommand()
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(new GetStyleRepositoryCommand.GetStyleRepositoryOptions(
                type: "nested-directory",
                params: [file: styleDirectory],
                layerName: "states",
                styleName: "states-blue"
        ), reader, writer)
        String sld = writer.toString()
        assertNotNull(sld)
        assertTrue(sld.contains("<sld:UserLayer>"))
    }

    @Test
    void sqlite() {
        File databaseFile = createTemporaryFile("states", "db")
        save("sqlite", [file: databaseFile], "states", "states")
        save("sqlite", [file: databaseFile], "states", "states-blue")
        Sql sql = Sql.newInstance("jdbc:sqlite:${databaseFile.absolutePath}", "org.sqlite.JDBC")

        assertEquals(2, sql.rows("select * from layer_styles where f_table_name = :layerName", [layerName: "states"]).size())
        GetStyleRepositoryCommand cmd = new GetStyleRepositoryCommand()
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(new GetStyleRepositoryCommand.GetStyleRepositoryOptions(
                type: "sqlite",
                params: [file: databaseFile],
                layerName: "states",
                styleName: "states-blue"
        ), reader, writer)
        String sld = writer.toString()
        assertNotNull(sld)
        assertTrue(sld.contains("<sld:UserLayer>"))
    }

    @Test
    void h2() {
        File databaseFile = createTemporaryFile("states", "db")
        save("h2", [file: databaseFile], "states", "states")
        save("h2", [file: databaseFile], "states", "states-blue")
        Sql sql = Sql.newInstance("jdbc:h2:file:${databaseFile.absolutePath}", "org.h2.Driver")

        assertEquals(2, sql.rows("select * from layer_styles where f_table_name = :layerName", [layerName: "states"]).size())
        GetStyleRepositoryCommand cmd = new GetStyleRepositoryCommand()
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(new GetStyleRepositoryCommand.GetStyleRepositoryOptions(
                type: "h2",
                params: [file: databaseFile],
                layerName: "states",
                styleName: "states-blue"
        ), reader, writer)
        String sld = writer.toString()
        assertNotNull(sld)
        assertTrue(sld.contains("<sld:UserLayer>"))
    }

    @Test
    void run() {
        File styleDirectory = createDir("styles")
        save("directory", [file: styleDirectory], "states", "states")
        save("directory", [file: styleDirectory], "states", "states-blue")

        File outputFile = createTemporaryFile("output","sld")

        runApp([
               "style repository get",
                "-t", "directory",
                "-o", "file=${styleDirectory.absolutePath}",
                "-l", "states",
                "-s", "states-blue",
                "-f", "${outputFile.absolutePath}"
        ], "")

        String sld = outputFile.text
        assertNotNull(sld)
        assertTrue(sld.contains("<sld:UserLayer>"))
    }

    private void save(String type, Map typeOptions, String layerName, String styleName) {
        File statesSld = createTemporaryFile("states","sld")
        statesSld.text = createStyle(new Color("blue"))
        SaveStyleRepositoryCommand cmd = new SaveStyleRepositoryCommand()
        SaveStyleRepositoryCommand.SaveStyleRepositoryOptions options = new SaveStyleRepositoryCommand.SaveStyleRepositoryOptions(
                type: type,
                params: typeOptions,
                styleFile: statesSld,
                layerName: layerName,
                styleName: styleName
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
    }

    private String createStyle(Color color) {
        new SLDWriter().write(new Fill(color))
    }

}
