package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.H2
import org.geocommands.vector.CopyCommand.CopyOptions
import org.geocommands.vector.DatabaseCreateIndexCommand.DatabaseCreateIndexOptions
import org.geocommands.vector.DatabaseIndexDeleteCommand.DatabaseIndexDeleteOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * The DatabaseIndexDeleteCommand Unit Test
 * @author Jared Erickson
 */
class DatabaseIndexDeleteCommandTest extends BaseTest {

    private File createDB() {
        File file = createTemporaryFile("points50", ".db")
        // Copy database so we can modify it
        CopyCommand copyCmd = new CopyCommand()
        CopyOptions copyOptions = new CopyOptions(
                inputWorkspace: getResource("points50.properties"),
                outputWorkspace: file,
                outputLayer: "points50"
        )
        copyCmd.execute(copyOptions)
        // Add spatial index
        DatabaseCreateIndexCommand createIndexCmd = new DatabaseCreateIndexCommand()
        DatabaseCreateIndexOptions createIndexOptions = new DatabaseCreateIndexOptions(
                databaseWorkspace: file,
                indexName: "geom_index",
                layerName: "points50",
                fieldNames: ["the_geom"],
                unique: false
        )
        createIndexCmd.execute(createIndexOptions)
        file
    }

    @Test void execute() {
        File dbFile = createDB()
        Database db = new H2(dbFile)
        assertTrue([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
        DatabaseIndexDeleteCommand cmd = new DatabaseIndexDeleteCommand()
        DatabaseIndexDeleteOptions options = new DatabaseIndexDeleteOptions(
                databaseWorkspace: dbFile,
                indexName: "geom_index",
                layerName: "points50"
        )
        cmd.execute(options)
        assertFalse([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
    }

    @Test void run() {
        File dbFile = createDB()
        Database db = new H2(dbFile)
        assertTrue([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
        runApp([
                "vector database index delete",
                "-w", dbFile.absolutePath,
                "-i", "geom_index",
                "-l", "points50"
        ], "")
        assertFalse([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
    }
}
