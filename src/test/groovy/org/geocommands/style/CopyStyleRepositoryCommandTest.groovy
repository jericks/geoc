package org.geocommands.style

import geoscript.filter.Color
import geoscript.style.Fill
import geoscript.style.io.SLDWriter
import groovy.sql.Sql
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class CopyStyleRepositoryCommandTest extends BaseTest {

    @Test
    void directoryToSqlite() {
        File styleDirectory = createDir("styles")
        save("directory", [file: styleDirectory], "states", "states")
        save("directory", [file: styleDirectory], "roads", "roads")
        save("directory", [file: styleDirectory], "parcels", "parcels")
        save("directory", [file: styleDirectory], "hydro", "hydro")
        assertTrue(styleDirectory.list().length == 4)

        File databaseFile = createTemporaryFile("styles", "db")
        Sql sql = Sql.newInstance("jdbc:sqlite:${databaseFile.absolutePath}", "org.sqlite.JDBC")

        CopyStyleRepositoryCommand cmd = new CopyStyleRepositoryCommand()
        cmd.execute(new CopyStyleRepositoryCommand.CopyStyleRepositoryOptions(
                inputType: "directory",
                inputParams: [file: styleDirectory],
                outputType: "sqlite",
                outputParams: [file: databaseFile]
        ))
        assertEquals(4, sql.rows("select * from layer_styles").size())
    }

    @Test
    void runSqliteToH2() {
        File sqliteFile = createTemporaryFile("styles", "db")
        save("sqlite", [file: sqliteFile], "states", "states")
        save("sqlite", [file: sqliteFile], "roads", "roads")
        save("sqlite", [file: sqliteFile], "parcels", "parcels")
        save("sqlite", [file: sqliteFile], "hydro", "hydro")
        Sql.withInstance("jdbc:sqlite:${sqliteFile.absolutePath}", "org.sqlite.JDBC") {Sql sql ->
            assertEquals(4, sql.rows("select * from layer_styles").size())
        }

        File h2File = createTemporaryFile("h2_styles", "db")

        println runApp([
            "style repository copy",
            "-t", "sqlite",
            "-p", "file=${sqliteFile.absolutePath}",
            "-o", "h2",
            "-r", "file=${h2File.absolutePath}"
        ], "")

        Sql.withInstance("jdbc:h2:file:${h2File.absolutePath}", "org.h2.Driver") {Sql sql ->
            assertEquals(4, sql.rows("select * from layer_styles").size())
        }
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
