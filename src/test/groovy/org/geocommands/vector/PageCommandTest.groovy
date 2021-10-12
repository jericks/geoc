package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.layer.io.CsvReader
import org.geocommands.BaseTest
import org.geocommands.vector.PageCommand.PageOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The PageCommand Unit Test
 * @author Jared Erickson
 */
class PageCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes1")
        PageCommand cmd = new PageCommand()
        // 1,2,3
        PageOptions options = new PageOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                start: 0,
                max: 3
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.8, Nevada", f["title"]
            if (i == 1) assertEquals "M 3.0, Northern California", f["title"]
            if (i == 2) assertEquals "M 2.5, Hawaii region, Hawaii", f["title"]
        }
        // 4,5,6
        options = new PageOptions(
                inputWorkspace: file.absolutePath,
                outputWorkspace: shpFile,
                start: 3,
                max: 3
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 5.2, northwest of Australia", f["title"]
            if (i == 1) assertEquals "M 2.7, Southern Alaska", f["title"]
            if (i == 2) assertEquals "M 4.7, near the east coast of Honshu, Japan", f["title"]
        }
    }

    @Test
    void executeWithText() {
        StringReader reader = getStringReader("earthquakes.csv")
        StringWriter writer = new StringWriter()
        PageCommand cmd = new PageCommand()
        // 1,2,3
        PageOptions options = new PageOptions(
                start: 0,
                max: 3
        )
        cmd.execute(options, reader, writer)
        Layer layer = new CsvReader().read(writer.toString())
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.8, Nevada", f["title"]
            if (i == 1) assertEquals "M 3.0, Northern California", f["title"]
            if (i == 2) assertEquals "M 2.5, Hawaii region, Hawaii", f["title"]
        }
        // 4,5,6
        reader = getStringReader("earthquakes.csv")
        writer = new StringWriter()
        options = new PageOptions(
                start: 3,
                max: 3
        )
        cmd.execute(options, reader, writer)
        layer = new CsvReader().read(writer.toString())
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 5.2, northwest of Australia", f["title"]
            if (i == 1) assertEquals "M 2.7, Southern Alaska", f["title"]
            if (i == 2) assertEquals "M 4.7, near the east coast of Honshu, Japan", f["title"]
        }
    }

    @Test
    void runWithFiles() {
        // 1,2,3
        File file = getResource("earthquakes.properties")
        File shpFile = createTemporaryShapefile("earthquakes2")
        runApp([
                "vector page",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-t", "0",
                "-m", "3"
        ], "")
        Layer layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.8, Nevada", f["title"]
            if (i == 1) assertEquals "M 3.0, Northern California", f["title"]
            if (i == 2) assertEquals "M 2.5, Hawaii region, Hawaii", f["title"]
        }
        // 4,5,6
        shpFile = createTemporaryShapefile("earthquakes3")
        runApp([
                "vector page",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-t", "3",
                "-m", "3"
        ], "")
        layer = new Shapefile(shpFile)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 5.2, northwest of Australia", f["title"]
            if (i == 1) assertEquals "M 2.7, Southern Alaska", f["title"]
            if (i == 2) assertEquals "M 4.7, near the east coast of Honshu, Japan", f["title"]
        }
    }

    @Test
    void runWithText() {
        // 1,2,3
        StringReader reader = getStringReader("earthquakes.csv")
        String result = runApp([
                "vector page",
                "-t", "0",
                "-m", "3"
        ], reader.text)
        Layer layer = new CsvReader().read(result)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 2.8, Nevada", f["title"]
            if (i == 1) assertEquals "M 3.0, Northern California", f["title"]
            if (i == 2) assertEquals "M 2.5, Hawaii region, Hawaii", f["title"]
        }
        // 4,5,6
        reader = getStringReader("earthquakes.csv")
        result = runApp([
                "vector page",
                "-t", "3",
                "-m", "3"
        ], reader.text)
        layer = new CsvReader().read(result)
        assertEquals 3, layer.count
        layer.cursor.eachWithIndex { f, i ->
            if (i == 0) assertEquals "M 5.2, northwest of Australia", f["title"]
            if (i == 1) assertEquals "M 2.7, Southern Alaska", f["title"]
            if (i == 2) assertEquals "M 4.7, near the east coast of Honshu, Japan", f["title"]
        }
    }
}
