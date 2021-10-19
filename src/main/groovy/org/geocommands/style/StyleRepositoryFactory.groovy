package org.geocommands.style

import geoscript.style.DatabaseStyleRepository
import geoscript.style.DirectoryStyleRepository
import geoscript.style.NestedDirectoryStyleRepository
import geoscript.style.StyleRepository
import geoscript.workspace.H2
import groovy.sql.Sql

class StyleRepositoryFactory {

    static StyleRepository getStyleRepository(String type, Map options) {
        if (type.equalsIgnoreCase("directory")) {
            new DirectoryStyleRepository(getFile(options.file))
        } else if (type.equalsIgnoreCase("nested-directory")) {
            new NestedDirectoryStyleRepository(getFile(options.file))
        } else if (type.equalsIgnoreCase("sqlite")) {
            File file = getFile(options.file)
            Sql sql = Sql.newInstance("jdbc:sqlite:${file.absolutePath}", "org.sqlite.JDBC")
            DatabaseStyleRepository.forSqlite(sql)
        } else if (type.equalsIgnoreCase("h2")) {
            File file = getFile(options.file)
            H2 h2 = new H2(file)
            Sql sql = h2.sql
            DatabaseStyleRepository.forH2(sql)
        } else if (type.equalsIgnoreCase("postgres")) {
            String server = options.server ?: 'localhost'
            String database = options.database
            String port = options.port ?: '5432'
            String userName = options.userName
            String password = options.password
            Sql sql = Sql.withInstance("jdbc:postgres://${server}:${port}/${database}", userName, password, "org.postgresql.Driver")
            DatabaseStyleRepository.forPostgres(sql)
        }
    }

    private static File getFile(Object file) {
        if (file instanceof File) {
            file
        } else {
            new File(file.toString())
        }
    }

}
