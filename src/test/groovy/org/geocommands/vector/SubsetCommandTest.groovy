package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.SubsetCommand.SubsetOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The SubsetCommand Unit Test
 * @author Jared Erickson
 */
class SubsetCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes")
        SubsetCommand cmd = new SubsetCommand()
        SubsetOptions options = new SubsetOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                sort: ["date ASC", "title ASC"],
                start: 5,
                max: 10
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.6, Puerto Rico region", f["title"]
            if (i == 9) assertEquals "M 3.0, Alaska Peninsula", f["title"]
        }
        assertEquals 10, layer.count
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("earthquakes.csv")
        StringWriter writer = new StringWriter()
        SubsetCommand cmd = new SubsetCommand()
        SubsetOptions options = new SubsetOptions(
                sort: ["date ASC", "title ASC"],
                start: 5,
                max: 10
        )
        cmd.execute(options, reader, writer)
        Layer layer = new CsvReader().read(writer.toString())
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.6, Puerto Rico region", f["title"]
            if (i == 9) assertEquals "M 3.0, Alaska Peninsula", f["title"]
        }
        assertEquals 10, layer.count
    }

    @Test
    void runWithFiles() {
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes")
        runApp([
                "vector subset",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-s", "date ASC",
                "-s", "title ASC",
                "-t", "5",
                "-m", "10"
        ], "")
        Layer layer = new Shapefile(shpFile)
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.6, Puerto Rico region", f["title"]
            if (i == 9) assertEquals "M 3.0, Alaska Peninsula", f["title"]
        }
        assertEquals 10, layer.count
    }

    @Test
    void runWithText() {
        StringReader reader = getStringReader("earthquakes.csv")
        String result = runApp([
                "vector subset",
                "-s", "date ASC",
                "-s", "title ASC",
                "-t", "5",
                "-m", "10"
        ], reader.text)
        Layer layer = new CsvReader().read(result)
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.6, Puerto Rico region", f["title"]
            if (i == 9) assertEquals "M 3.0, Alaska Peninsula", f["title"]
        }
        assertEquals 10, layer.count
    }
}
