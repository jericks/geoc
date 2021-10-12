package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.DataStoreListCommand.DataStoreListOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The DataStoreListCommand Unit Test
 * @author Jared Erickson
 */
class DataStoreListCommandTest extends BaseTest {

    @Test
    void executeToCsv() {
        DataStoreListCommand cmd = new DataStoreListCommand()
        DataStoreListOptions options = new DataStoreListOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        String str = w.toString()
        assertTrue str.contains("H2")
        assertTrue str.contains("PostGIS")
        assertTrue str.contains("Shapefile")
    }

    @Test
    void runAsCommandLine() {
        String str = runApp(["vector datastorelist"], "")
        assertTrue str.contains("H2")
        assertTrue str.contains("PostGIS")
        assertTrue str.contains("Shapefile")
    }
}
