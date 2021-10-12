package org.geocommands.vector

import geoscript.workspace.Database
import geoscript.workspace.H2
import org.geocommands.vector.CopyCommand.CopyOptions
import org.geocommands.vector.DatabaseCreateIndexCommand.DatabaseCreateIndexOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The DatabaseCreateIndexCommand Unit Test
 * @author Jared Erickson
 */
class DatabaseCreateIndexCommandTest extends BaseTest {

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
        DatabaseCreateIndexCommand cmd = new DatabaseCreateIndexCommand()
        DatabaseCreateIndexOptions options = new DatabaseCreateIndexOptions(
                databaseWorkspace: dbFile,
                indexName: "geom_index",
                layerName: "points50",
                fieldNames: ["the_geom"],
                unique: false
        )
        cmd.execute(options)
        Database db = new H2(dbFile)
        assertTrue([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
    }

    @Test void run() {
        File dbFile = createDB()
        runApp([
            "vector database index create",
                "-w", dbFile.absolutePath,
                "-i", "geom_index",
                "-l", "points50",
                "-f", "the_geom"
        ], "")
        Database db = new H2(dbFile)
        assertTrue([name:"geom_index", unique:false, attributes:["the_geom"]] in db.getIndexes("points50"))
    }

}
