package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.DataStoreParametersCommand.DataStoreParametersOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

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
skipScan""", w.toString()

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
skipScan
""", str
    }

}
