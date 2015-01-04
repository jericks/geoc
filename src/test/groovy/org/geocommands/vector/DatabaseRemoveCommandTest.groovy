package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.H2
import org.geocommands.vector.DatabaseRemoveCommand.DatabaseRemoveOptions
import org.geocommands.vector.CopyCommand.CopyOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The DatabaseRemoveCommand Unit Test
 * @author Jared Erickson
 */
class DatabaseRemoveCommandTest extends BaseTest {

    private File createDB() {
        File file = createTemporaryFile("points50", ".db")
        CopyCommand cmd = new CopyCommand()
        CopyOptions options = new CopyOptions(
                inputWorkspace: getResource("points50.properties"),
                outputWorkspace: file,
                outputLayer: "points50"
        )
        cmd.execute(options)
        file
    }

    @Test void execute() {
        File dbFile = createDB()
        Database db = new H2(dbFile)
        assertTrue(db.has("points50"))
        DatabaseRemoveCommand cmd = new DatabaseRemoveCommand()
        DatabaseRemoveOptions options = new DatabaseRemoveOptions(
                databaseWorkspace: dbFile,
                layerName: "points50"
        )
        cmd.execute(options)
        assertFalse(db.has("points50"))
    }

    @Test void run() {
        File dbFile = createDB()
        Database db = new H2(dbFile)
        assertTrue(db.has("points50"))
        String result = runApp([
                "vector database remove",
                "-w", dbFile.absolutePath,
                "-l", "points50"
        ], "")
        assertFalse(db.has("points50"))
    }
}
