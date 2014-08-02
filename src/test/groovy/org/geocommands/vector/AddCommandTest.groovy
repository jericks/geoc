package org.geocommands.vector

import geoscript.feature.Feature
import geoscript.layer.Layer
import geoscript.workspace.Property
import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.AddCommand.AddOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The AddCommand UnitTest
 * @author Jared Erickson
 */
class AddCommandTest extends BaseTest {

    @Test
    void execute() {
        AddCommand cmd = new AddCommand()
        File file = getCopiedResource("points.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        AddOptions options = new AddOptions(
                inputWorkspace: file.absolutePath,
                values: [
                        the_geom: "POINT (3 3)",
                        distance: 3,
                        name    : "Number 4"
                ]
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        layer = new Property(file.parentFile).get(file.name)
        assertEquals 4, layer.count
        Feature f = layer.getFeatures("name = 'Number 4'")[0]
        assertEquals f.geom.wkt, "POINT (3 3)"
        assertEquals f['distance'], "3"
        assertEquals f['name'], "Number 4"
    }

    @Test
    void executeWithCsv() {
        AddCommand cmd = new AddCommand()
        AddOptions options = new AddOptions(values: [
                the_geom: "POINT (3 3)",
                distance: 3,
                name    : "Number 4"
        ])
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), w)
        Layer layer = getLayerFromCsv(w.toString())
        assertEquals 4, layer.count
        Feature f = layer.getFeatures("name = 'Number 4'")[0]
        assertEquals f.geom.wkt, "POINT (3 3)"
        assertEquals f['distance'], "3"
        assertEquals f['name'], "Number 4"
    }

    @Test
    void runAsCommandLine() {
        File file = getCopiedResource("points.properties")
        Layer layer = new Property(file.parentFile).get(file.name)
        assertEquals 3, layer.count
        App.main([
                "vector add",
                "-i", file.absolutePath,
                "-v", "the_geom=POINT (3 3)",
                "-v", "distance=3",
                "-v", "name=Number 4"
        ] as String[])
        layer = new Property(file.parentFile).get(file.name)
        assertEquals 4, layer.count
        Feature f = layer.getFeatures("name = 'Number 4'")[0]
        assertEquals f.geom.wkt, "POINT (3 3)"
        assertEquals f['distance'], "3"
        assertEquals f['name'], "Number 4"

        String output = runApp(["vector add",
                                "-v", "the_geom=POINT (3 3)",
                                "-v", "distance=3",
                                "-v", "name=Number 4"
        ], readCsv("points.csv").text)
        layer = getLayerFromCsv(output)
        assertEquals 4, layer.count
        f = layer.getFeatures("name = 'Number 4'")[0]
        assertEquals f.geom.wkt, "POINT (3 3)"
        assertEquals f['distance'], "3"
        assertEquals f['name'], "Number 4"
    }

}
