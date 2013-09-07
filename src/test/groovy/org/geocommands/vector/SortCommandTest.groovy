package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.SortCommand.SortOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The SortCommand Unit Test
 * @author Jared Erickson
 */
class SortCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        SortCommand cmd = new SortCommand()
        SortOptions options = new SortOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                sort: ["distance ASC"]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals 1, f["distance"] as int
            if (i == 1) assertEquals 2, f["distance"] as int
            if (i == 2) assertEquals 5, f["distance"] as int
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("points.csv")
        StringWriter writer = new StringWriter()
        SortCommand cmd = new SortCommand()
        SortOptions options = new SortOptions(
                sort: ["distance DESC"]
        )
        cmd.execute(options, reader, writer)
        Layer layer = new CsvReader().read(writer.toString())
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals 5, f["distance"] as int
            if (i == 1) assertEquals 2, f["distance"] as int
            if (i == 2) assertEquals 1, f["distance"] as int
        }
    }

    @Test
    void runWithFiles() {
        File file = getResource("points.properties")
        File shpFile = createTemporaryShapefile("points")
        runApp([
                "vector sort",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-s", "distance ASC"
        ], "")
        Layer layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals 1, f["distance"] as int
            if (i == 1) assertEquals 2, f["distance"] as int
            if (i == 2) assertEquals 5, f["distance"] as int
        }
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("points.csv")
        String result = runApp([
                "vector sort",
                "-s", "distance DESC"
        ], reader.text)
        Layer layer = new CsvReader().read(result)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals 5, f["distance"] as int
            if (i == 1) assertEquals 2, f["distance"] as int
            if (i == 2) assertEquals 1, f["distance"] as int
        }
    }
}
