package org.geocommands.vector

import org.geocommands.App
import org.geocommands.BaseTest
import org.geocommands.vector.DrawLayerCommand.DrawLayerOptions
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The DrawLayerCommand Unit Test
 * @author Jared Erickson
 */
class DrawLayerCommandTest extends BaseTest {

    private File createSldFile(File layerFile) {
        DefaultStyleCommand cmd = new DefaultStyleCommand()
        DefaultStyleCommand.DefaultStyleOptions options = new DefaultStyleCommand.DefaultStyleOptions(inputWorkspace: layerFile)
        String sld = cmd.execute(options)
        File file = File.createTempFile("style", ".sld")
        file.write(sld)
        file
    }

    @Test
    void execute() {
        DrawLayerCommand cmd = new DrawLayerCommand()
        File file = getResource("polygons.properties")
        File imgFile = File.createTempFile("polygons_", ".png")
        File sldFile = createSldFile(file)
        DrawLayerOptions options = new DrawLayerOptions(
                inputWorkspace: file.absolutePath,
                width: 400,
                height: 400,
                file: imgFile,
                type: "png",
                sldFile: sldFile
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        assertTrue imgFile.exists()
    }

    @Test
    void executeWithCsv() {
        DrawLayerCommand cmd = new DrawLayerCommand()
        File imgFile = File.createTempFile("polygons_", ".jpeg")
        DrawLayerOptions options = new DrawLayerOptions(
                width: 500,
                height: 500,
                file: imgFile,
                type: "jpeg",
                bounds: "1 1 7 7"
        )
        StringWriter w = new StringWriter()
        cmd.execute(options, readCsv("polygons.csv"), w)
        assertTrue imgFile.exists()
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("polygons.properties")
        File imgFile = File.createTempFile("polygons_", ".pdf")
        App.main([
                "vector draw",
                "-i", file.absolutePath,
                "-w", "800",
                "-h", "700",
                "-f", imgFile.absolutePath,
                "-t", "pdf"
        ] as String[])
        assertTrue imgFile.exists()

        imgFile = File.createTempFile("polygons_", ".svg")
        String output = runApp([
                "vector draw",
                "-w", "800",
                "-h", "700",
                "-f", imgFile.absolutePath,
                "-t", "svg"
        ], readCsv("polygons.csv").text)
        assertTrue imgFile.exists()
    }
}
