package org.geocommands

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertEquals

class VersionCommandTest extends BaseTest {

    @Test
    void execute() {
        VersionCommand cmd = new VersionCommand()
        VersionCommand.VersionOptions options = new VersionCommand.VersionOptions()
        StringWriter w = new StringWriter()
        cmd.execute(options, new StringReader(""), w)
        String str = w.toString()
        assertEquals getExpectedVersion(), str
    }

    @Test
    void runAsCommandLine() {
        String str = runApp(["version"], "")
        assertEquals getExpectedVersion(), str.trim()
    }

    private String getExpectedVersion() {
        String version = null
        VersionCommand.class.getClassLoader().getResource("application.properties").withInputStream { InputStream inputStream ->
            Properties properties = new Properties()
            properties.load(inputStream)
            version = properties.getProperty("version")
        }
        version
    }

}
