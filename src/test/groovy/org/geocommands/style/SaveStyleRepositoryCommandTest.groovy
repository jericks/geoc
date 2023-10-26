package org.geocommands.style

import geoscript.filter.Color
import geoscript.style.Fill
import geoscript.style.io.SLDWriter
import groovy.sql.Sql
import org.geocommands.style.SaveStyleRepositoryCommand.SaveStyleRepositoryOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class SaveStyleRepositoryCommandTest extends BaseTest {

    @Test
    void saveToDirectory() {

        File styleDirectory = createDir("styles")
        File statesSld = createTemporaryFile("states","sld")
        statesSld.text = createStyle(new Color("blue"))

        SaveStyleRepositoryCommand cmd = new SaveStyleRepositoryCommand()
        SaveStyleRepositoryOptions options = new SaveStyleRepositoryOptions(
                type: "directory",
                params: ["file": styleDirectory],
                styleFile: statesSld,
                layerName: "states",
                styleName: "states"
        )
        File expectedFile = new File(styleDirectory, "states.sld")
        assertFalse(expectedFile.exists())
        cmd.execute(options, new StringReader(""), new StringWriter())
        assertTrue(expectedFile.exists())

    }

    @Test
    void saveToNestedDirectory() {

        File styleDirectory = createDir("styles")
        File statesSld = createTemporaryFile("states","sld")
        statesSld.text = createStyle(new Color("blue"))

        SaveStyleRepositoryCommand cmd = new SaveStyleRepositoryCommand()
        SaveStyleRepositoryOptions options = new SaveStyleRepositoryOptions(
                type: "nested-directory",
                params: ["file": styleDirectory],
                styleFile: statesSld,
                layerName: "states",
                styleName: "states-blue"
        )
        File expectedFile = new File(styleDirectory, "states/states-blue.sld")
        assertFalse(expectedFile.exists())
        cmd.execute(options, new StringReader(""), new StringWriter())
        assertTrue(expectedFile.exists())

    }

    @Test
    void saveToSqlite() {

        File databaseFile = createTemporaryFile("states", "db")
        File statesSld = createTemporaryFile("states","sld")
        statesSld.text = createStyle(new Color("blue"))

        SaveStyleRepositoryCommand cmd = new SaveStyleRepositoryCommand()
        SaveStyleRepositoryOptions options = new SaveStyleRepositoryOptions(
                type: "sqlite",
                params: ["file": databaseFile],
                styleFile: statesSld,
                layerName: "states",
                styleName: "states-blue"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())

        Sql sql = Sql.newInstance("jdbc:sqlite:${databaseFile.absolutePath}", "org.sqlite.JDBC")
        List results = sql.rows(
            "SELECT * FROM layer_styles WHERE f_table_name = :name AND styleName = :styleName",
            [name: "states", styleName: "states-blue"]
        )
        assertEquals(1, results.size())

    }

    @Test
    void runWithStrings() {

        File databaseFile = createTemporaryFile("states", "db")
        String sld = createStyle(new Color("blue"))

        runApp([
                "style repository save",
                "-t", "h2",
                "-o", "file=${databaseFile.absolutePath}",
                "-l", "states",
                "-s", "states-blue"
        ], sld)

        Sql sql = Sql.newInstance("jdbc:h2:file:${databaseFile.absolutePath}", "org.h2.Driver")
        List results = sql.rows(
                "SELECT * FROM layer_styles WHERE f_table_name = :name AND styleName = :styleName",
                [name: "states", styleName: "states-blue"]
        )
        assertEquals(1, results.size())

    }

    private String createStyle(Color color) {
        new SLDWriter().write(new Fill(color))
    }

}
