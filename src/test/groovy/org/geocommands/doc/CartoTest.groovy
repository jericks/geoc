package org.geocommands.doc

import org.junit.jupiter.api.Test

class CartoTest extends DocTest {

    @Test
    void simpleJson() {
        String command = "geoc carto map -t json -c src/test/resources/carto/simple.json -o target/carto_simple_json.png"
        String result = runApp(command, "")
        writeTextFile("geoc_carto_map_simple_json_command", command)
        writeTextFile("geoc_carto_map_simple_json_command_output", result)
        copyFile(new File("target/carto_simple_json.png"), new File("src/main/docs/images/geoc_carto_map_simple_json_command.png"))
        copyFile(new File("src/test/resources/carto/simple.json"), new File("src/main/docs/output/geoc_carto_map_simple_json_command.json"))
    }

    @Test
    void simpleXml() {
        String command = "geoc carto map -t xml -c src/test/resources/carto/simple.xml -o target/carto_simple_xml.png"
        String result = runApp(command, "")
        writeTextFile("geoc_carto_map_simple_xml_command", command)
        writeTextFile("geoc_carto_map_simple_xml_command_output", result)
        copyFile(new File("target/carto_simple_xml.png"), new File("src/main/docs/images/geoc_carto_map_simple_xml_command.png"))
        copyFile(new File("src/test/resources/carto/simple.xml"), new File("src/main/docs/output/geoc_carto_map_simple_xml_command.xml"))
    }

}
