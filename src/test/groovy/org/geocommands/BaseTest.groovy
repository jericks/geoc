package org.geocommands

import geoscript.layer.Layer
import geoscript.layer.io.CsvReader
import org.junit.jupiter.api.io.TempDir

import static junit.framework.Assert.assertEquals

/**
 * A base class for Unit Tests.
 * @author Jared Erickson
 */
class BaseTest {

    @TempDir
    File folder

    public static String NEW_LINE = System.getProperty("line.separator")

    void assertStringsEqual(String expected, String actual) {
        assertStringsEqual(expected, actual, false)
    }

    void assertStringsEqual(String expected, String actual, boolean trim, boolean stripXmlNamespaces = false) {
        StringReader expectedReader = new StringReader(expected)
        StringReader actualReader = new StringReader(actual)
        List<String> expectedLines = expectedReader.readLines()
        List<String> actualLines = actualReader.readLines()
        assertEquals(expectedLines.size(), actualLines.size())
        expectedLines.eachWithIndex { String exp, int i ->
            String act = actualLines[i]
            if (trim) {
                exp = exp.trim()
                act = act.trim()
            }
            if (stripXmlNamespaces) {
                exp = stripXmlNS(exp)
                act = stripXmlNS(act)
            }
            assertEquals(exp, act)
        }
    }

    String stripXmlNS(String str) {
        str.replaceAll("xmlns.*?(\"|\').*?(\"|\')", "")
    }

    File getResource(String resource) {
        new File(getClass().getClassLoader().getResource(resource).toURI())
    }

    File getCopiedResource(String resource) {
        File file = getResource(resource)
        List names = splitFileName(file.name)
        File tempFile = File.createTempFile(names[0], ".${names[1]}")
        tempFile.write(file.text)
        tempFile
    }

    private List splitFileName(String name) {
        int i = name.lastIndexOf(".")
        [
                name.substring(0, i),
                name.substring(i + 1)
        ]
    }

    File createDir(String name) {
        File file = new File(folder, name)
        file.mkdir()
        file
    }

    File createTemporaryShapefile(String name) {
        File dir = createDir(name)
        new File(dir, "${name}.shp")
    }

    File createTemporaryFile(String name, String ext) {
        File dir = createDir(name)
        new File(dir, "${name}.${ext}")
    }

    StringReader readCsv(String resource) {
        new StringReader(getResource(resource).text)
    }

    StringReader getStringReader(String resource) {
        new StringReader(getResource(resource).text)
    }

    Layer getLayerFromCsv(String csv) {
        new CsvReader().read(csv)
    }

    String runApp(List args, String input = null) {
        // Save normal System.in and System.out
        InputStream sysin = System.in
        OutputStream sysout = System.out
        OutputStream syserr = System.err
        // Replace System.in with input text
        System.in = new ByteArrayInputStream(input.getBytes("UTF-8"))
        // Replace System.out with OutputStream we can capture
        OutputStream out = new ByteArrayOutputStream()
        System.out = new PrintStream(out)
        // Replace System.err with OutputStream we can capture
        OutputStream err = new ByteArrayOutputStream()
        System.err = new PrintStream(err)
        // Run the app with the List of arguments
        App.main(args as String[])
        // Replace normal System.in and System.out
        System.in = sysin
        System.out = sysout
        System.err = syserr
        // Return the captured output
        out.toString() ?: err.toString()
    }

}
