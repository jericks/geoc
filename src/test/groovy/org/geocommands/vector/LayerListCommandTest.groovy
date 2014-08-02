package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.LayerListCommand.LayerListOptions
import org.junit.Test

/**
 * The LayerListCommand Unit Test
 * @author Jared Erickson
 */
class LayerListCommandTest extends BaseTest {

    @Test
    void execute() {
        LayerListCommand cmd = new LayerListCommand()
        LayerListOptions options = new LayerListOptions(
                inputWorkspace: "dbtype=h2 database=src/test/resources/h2.db"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        assertStringsEqual("""points
polygons""", writer.toString())
    }

    @Test
    void run() {
        String result = runApp([
                "vector list layers",
                "-i", "dbtype=h2 database=src/test/resources/h2.db"
        ], "")
        assertStringsEqual("""points
polygons""", result)
    }

}
