package org.geocommands.vector

import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import geoscript.workspace.Property
import geoscript.workspace.Workspace
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

/**
 * The AppendCommand Unit Test
 * @author Jared Erickson
 */
class AppendCommandTest extends BaseTest {

    private Layer createLayer(Workspace w, String name) {
        Schema schema = new Schema(name, [
            new Field("the_geom","Point","EPSG:4326"),
            new Field("name","String"),
            new Field("distance","double")
        ])
        Layer layer = w.create(schema)
        layer.add([the_geom: "POINT(100 100)", name: "house", distance: 2.4])
        layer.add([the_geom: "POINT(200 200)", name: "work", distance: 1.1])
        layer.add([the_geom: "POINT(300 300)", name: "school", distance: 4.5])
        layer
    }

    @Test void execute() {
        // Get the input layer
        File file = getCopiedResource("points.properties")
        // Get another Layer
        File anotherFile = File.createTempFile("points",".properties")
        Property property = new Property(anotherFile.parentFile)
        Layer anotherLayer = createLayer(property, anotherFile.name.replaceAll(".properties",""))
        // Set up options
        AppendCommand.AppendOptions options = new AppendCommand.AppendOptions(
            inputWorkspace: file.absolutePath,
            otherWorkspace: anotherFile.absolutePath
        )
        // Execute the command
        AppendCommand cmd = new AppendCommand()
        cmd.execute(options, new StringReader(""), new StringWriter())
        // Create Layer for the input layer
        Property inputProperty = new Property(file.parentFile)
        Layer inputLayer = inputProperty.get(file.name)
        // Make sure the append worked
        assertEquals 6, inputLayer.count
        assertEquals 1, inputLayer.getFeatures("name = 'Number 1'").size()
        assertEquals 1, inputLayer.getFeatures("name = 'house'").size()
    }

    @Test void executeWithCsv() {
        // Get another Layer
        File anotherFile = File.createTempFile("points",".properties")
        Property property = new Property(anotherFile.parentFile)
        Layer anotherLayer = createLayer(property, anotherFile.name.replaceAll(".properties",""))
        // Run the Command
        AppendCommand cmd = new AppendCommand()
        AppendCommand.AppendOptions options = new AppendCommand.AppendOptions(
            otherWorkspace: anotherFile.absolutePath
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 6, layer.count
        assertEquals 1, layer.getFeatures("name = 'Number 1'").size()
        assertEquals 1, layer.getFeatures("name = 'house'").size()
    }

    @Test void runAsCommandLine() {
        File file = getCopiedResource("points.properties")
        // Get another Layer
        File anotherFile = File.createTempFile("points",".properties")
        Property property = new Property(anotherFile.parentFile)
        Layer anotherLayer = createLayer(property, anotherFile.name.replaceAll(".properties",""))
        App.main([
            "vector append",
            "-i", file.absolutePath,
            "-k", anotherFile.absolutePath
        ] as String[])
        // Create Layer for the input layer
        Property inputProperty = new Property(file.parentFile)
        Layer inputLayer = inputProperty.get(file.name)
        // Make sure the append worked
        assertEquals 6, inputLayer.count
        assertEquals 1, inputLayer.getFeatures("name = 'Number 1'").size()
        assertEquals 1, inputLayer.getFeatures("name = 'house'").size()

        String output = runApp(["vector append","-k",anotherFile.absolutePath],readCsv("points.csv").text)
        Layer layer = getLayerFromCsv(output)
        // Make sure the append worked
        assertEquals 6, inputLayer.count
        assertEquals 1, inputLayer.getFeatures("name = 'Number 1'").size()
        assertEquals 1, inputLayer.getFeatures("name = 'house'").size()
    }

}
