package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.workspace.Property
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.UpdateFieldCommand.UpdateFieldOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

/**
 * The UpdateFieldCommand Unit Test
 * @author Jared Erickson
 */
class UpdateFieldCommandTest extends BaseTest {

    @Test
    void execute() {
        UpdateFieldCommand cmd = new UpdateFieldCommand()
        File file = getCopiedResource("points.properties")
        UpdateFieldOptions options = new UpdateFieldOptions(
                inputWorkspace: file.absolutePath,
                field: "name",
                value: "return 'Point ' + f.get('name').split(' ')[1]",
                script: true
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        assertEquals 1, layer.count("name = 'Point 1'")
        assertEquals 1, layer.count("name = 'Point 2'")
        assertEquals 1, layer.count("name = 'Point 3'")
    }

    @Test
    void executeWithCsv() {
        UpdateFieldCommand cmd = new UpdateFieldCommand()
        UpdateFieldOptions options = new UpdateFieldOptions(
                field: "distance",
                value: "10",
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        println w.toString()
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 3, layer.count
        assertEquals 3, layer.count("distance = '10'")
    }

    @Test
    void runAsCommandLine() {
        File file = getCopiedResource("points.properties")
        App.main([
                "vector updatefield",
                "-i", file.absolutePath,
                "-d", "name",
                "-v", "distance"
        ] as String[])
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        assertEquals 1, layer.count("name = '1'")
        assertEquals 1, layer.count("name = '2'")
        assertEquals 1, layer.count("name = '5'")

        String output = runApp(["vector updatefield", "-d", "name", "-v", "'Point'", "-f", "name = 'Number 1'"], readCsv("points.csv").text)
        layer = getLayerFromCsv(output)
        assertEquals 3, layer.count
        assertEquals 1, layer.count("name = 'Point'")
    }

}
