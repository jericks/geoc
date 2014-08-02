package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.workspace.Property
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DeleteCommand.DeleteOptions
import org.junit.Test

import static junit.framework.Assert.assertEquals

/**
 * The DeleteCommand Unit Test
 * @author Jared Erickson
 */
class DeleteCommandTest extends BaseTest {

    @Test
    void execute() {
        DeleteCommand cmd = new DeleteCommand()
        File file = getCopiedResource("points.properties")
        Layer layer = new Property(file.parentFile.absolutePath).get(file.name)
        assertEquals 3, layer.count
        DeleteOptions options = new DeleteOptions(
                inputWorkspace: file.absolutePath,
                filter: "name = 'Number 1'"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        layer = new Property(file.parentFile.absolutePath).get(file.name)
        assertEquals 2, layer.count
    }

    @Test
    void executeWithCsv() {
        DeleteCommand cmd = new DeleteCommand()
        DeleteOptions options = new DeleteOptions(filter: "name = 'Number 1'")
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 2, layer.count
    }

    @Test
    void runAsCommandLine() {
        File file = getCopiedResource("points.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        App.main([
                "vector delete",
                "-i", file.absolutePath,
                "-f", "name = 'Number 1'"
        ] as String[])
        layer = new Property(file.parentFile).get(file.name)
        assertEquals 2, layer.count

        String output = runApp(["vector delete", "-f", "name = 'Number 1'"], readCsv("points.csv").text)
        layer = getLayerFromCsv(output)
        assertEquals 2, layer.count
    }

}
