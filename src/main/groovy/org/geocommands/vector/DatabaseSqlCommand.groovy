package org.geocommands.vector

import au.com.bytecode.opencsv.CSVWriter
import geoscript.workspace.Database
import geoscript.workspace.Workspace
import groovy.sql.Sql
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Execute SQL commands against a Database Workspace.
 * @author Jared Erickson
 */
class DatabaseSqlCommand extends Command<DatabaseSqlOptions> {

    @Override
    String getName() {
        "vector database sql"
    }

    @Override
    String getDescription() {
        "Execute SQL commands against a Database Workspace"
    }

    @Override
    DatabaseSqlOptions getOptions() {
        new DatabaseSqlOptions()
    }

    @Override
    void execute(DatabaseSqlOptions options, Reader reader, Writer writer) throws Exception {
        Workspace workspace = Util.getWorkspace(options.databaseWorkspace)
        try {
            if (!(workspace instanceof Database)) {
                throw new IllegalArgumentException("Workspace must be a Database based Workspace!")
            }
            Database database = workspace as Database
            Sql sql = database.sql
            if (options.sql.trim().toLowerCase().startsWith("select")) {
                CSVWriter csv = new CSVWriter(writer, ',' as char, '"' as char)
                List columns = []
                sql.eachRow(options.sql, { def meta ->
                    columns = (1..meta.columnCount).collect { meta.getColumnLabel(it) }
                    csv.writeNext(columns as String[])
                }, { def row ->
                    csv.writeNext(columns.collect { row[it] } as String[])
                })
            } else {
                sql.execute(options.sql)
            }
            sql.close()
        }
        finally {
            workspace.close()
        }
    }

    static class DatabaseSqlOptions extends Options {

        @Option(name = "-w", aliases = "--database-workspace", usage = "The input workspace", required = true)
        String databaseWorkspace

        @Option(name = "-s", aliases = "--sql", usage = "The input layer", required = true)
        String sql

    }

}
