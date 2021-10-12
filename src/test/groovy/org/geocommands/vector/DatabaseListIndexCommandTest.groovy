package org.geocommands.vector

import org.geocommands.vector.CopyCommand.CopyOptions
import org.geocommands.vector.DatabaseListIndexCommand.DatabaseListIndexOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

/**
 * The DatabaseListIndexCommand Unit Test
 * @author Jared Erickson
 */
class DatabaseListIndexCommandTest extends BaseTest {

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
        DatabaseListIndexCommand cmd = new DatabaseListIndexCommand()
        DatabaseListIndexOptions options = new DatabaseListIndexOptions(
                databaseWorkspace: dbFile,
                layerName: "points50"
        )
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        assertStringsEqual("""name, unique, attributes
PRIMARY_KEY_1, true, fid""", writer.toString())
    }

    @Test void run() {
        File dbFile = createDB()
        String result = runApp([
                "vector database index list",
                "-w", dbFile.absolutePath,
                "-l", "points50",
                "-p"
        ], "")
        assertStringsEqual("""--------------------------------------------
| Name          | Unique   | Attributes    |
--------------------------------------------
| PRIMARY_KEY_1 | true     | fid           |
--------------------------------------------""", result)
    }

}
