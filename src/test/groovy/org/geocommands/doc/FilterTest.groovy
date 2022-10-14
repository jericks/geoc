package org.geocommands.doc

import org.junit.jupiter.api.Test

class FilterTest extends DocTest {

    @Test
    void cql2xml() {
        String command = "geoc filter cql2xml -c name=wa"
        String result = runApp(command, "")
        writeTextFile("geoc_filter_cql2xml_command", command)
        writeTextFile("geoc_filter_cql2xml_output", result)
    }

}
