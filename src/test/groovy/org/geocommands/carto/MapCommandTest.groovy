package org.geocommands.carto

import org.geocommands.BaseTest
import org.geocommands.carto.MapCommand.MapOptions
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertTrue

class MapCommandTest extends BaseTest {

    @Test void execute() {
        File outputFile = new File(folder, "carto_json_execute.png")
        MapOptions options = new MapOptions(
            type: "json",
            cartoFile: getResource("carto.json"),
            outputFile: outputFile
        )
        MapCommand cmd = new MapCommand()
        cmd.execute(options)
        assertTrue(outputFile.exists())
    }

    @Test void run() {
        File outputFile = new File(folder, "carto_xml_run.png")
        runApp([
                "carto map",
                "-t", "xml",
                "-o", outputFile.absolutePath
        ],getResource("carto.xml").text)
        assertTrue(outputFile.exists())
    }

}
