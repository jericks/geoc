package org.geocommands.vector

import org.geocommands.vector.DatabaseSqlCommand.DatabaseSqlOptions
import org.geocommands.BaseTest
import org.junit.Test

/**
 * The DatabaseSqlCommand Unit Test
 */
class DatabaseSqlCommandTest extends BaseTest {

    @Test void execute() {
        File file = createTemporaryFile("h2","db")
        CreateCommand createCmd = new CreateCommand()
        CreateCommand.CreateOptions createOptions = new CreateCommand.CreateOptions(
                fields: [
                        "the_geom": "POINT EPSG:4326",
                        "id"      : "int",
                        "name"    : "string"
                ],
                outputWorkspace: file.absolutePath,
                outputLayer: 'points'
        )
        createCmd.execute(createOptions)

        DatabaseSqlCommand cmd = new DatabaseSqlCommand()
        cmd.execute(new DatabaseSqlOptions(
                databaseWorkspace: file.absolutePath,
                sql: "insert into \"points\" (\"id\", \"the_geom\", \"name\") values (1, ST_GeomFromText('POINT(1 1)', 4326), 'point 1')"
        ))
        cmd.execute(new DatabaseSqlOptions(
                databaseWorkspace: file.absolutePath,
                sql: "insert into \"points\" (\"id\", \"the_geom\", \"name\") values (2, ST_GeomFromText('POINT(2 2)', 4326), 'point 2')"
        ))
        String result = cmd.execute(new DatabaseSqlOptions(
                databaseWorkspace: file.absolutePath,
                sql: 'select st_astext("the_geom") as "geom", "id", "name" from "points"'
        ))
        assertStringsEqual('"geom","id","name"' + NEW_LINE +
                '"POINT (1 1)","1","point 1"' + NEW_LINE +
                '"POINT (2 2)","2","point 2"', result)
    }

    @Test void run() {
        File file = createTemporaryFile("h2","db")
        runApp([
                "vector create",
                "-f", "the_geom=POINT EPSG:4326",
                "-f", "id=int",
                "-f", "name=String",
                "-o", file.absolutePath,
                "-r", "points"
        ],"")

        runApp([
                "vector database sql",
                "-w", file.absolutePath,
                "-s", "insert into \"points\" (\"id\", \"the_geom\", \"name\") values (1, ST_GeomFromText('POINT(1 1)', 4326), 'point 1')"
        ],"")
        runApp([
                "vector database sql",
                "-w", file.absolutePath,
                "-s", "insert into \"points\" (\"id\", \"the_geom\", \"name\") values (2, ST_GeomFromText('POINT(2 2)', 4326), 'point 2')"
        ],"")

        String result = runApp([
                "vector database sql",
                "-w", file.absolutePath,
                "-s", 'select st_astext("the_geom") as "geom", "id", "name" from "points"'
        ],"")
        assertStringsEqual('"geom","id","name"' + NEW_LINE +
                '"POINT (1 1)","1","point 1"' + NEW_LINE +
                '"POINT (2 2)","2","point 2"' + NEW_LINE + NEW_LINE, result)
    }

}
