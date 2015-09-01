package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.DataStoreParametersCommand.DataStoreParametersOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The DataStoreParametersCommand Unit Test
 * @author Jared Erickson
 */
class DataStoreParametersCommandTest extends BaseTest {

    @Test
    void executeToCsv() {
        DataStoreParametersCommand cmd = new DataStoreParametersCommand()
        DataStoreParametersOptions options = new DataStoreParametersOptions(name: "Shapefile")
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        println w.toString()
        assertStringsEqual """url
namespace
enable spatial index
create spatial index
charset
timezone
memory mapped buffer
cache and reuse memory maps
filetype
fstype""", w.toString()

    }

    @Test
    void runAsCommandLine() {
        String str = runApp(["vector datastoreparams", "-n", "Shapefile"], "")
        assertStringsEqual """url
namespace
enable spatial index
create spatial index
charset
timezone
memory mapped buffer
cache and reuse memory maps
filetype
fstype
""", str
    }

}
