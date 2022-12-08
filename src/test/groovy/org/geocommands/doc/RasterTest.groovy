package org.geocommands.doc

import org.junit.jupiter.api.Test

class RasterTest extends DocTest {

    @Test
    void info() {
        String command = "geoc raster info -i src/test/resources/earth.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_info_command", command)
        writeTextFile("geoc_raster_info_command_output", result)
    }

    @Test
    void getSize() {
        String command = "geoc raster size -i src/test/resources/earth.tif"
        String result = runApp(command, "")
        writeTextFile("geoc_raster_getsize_command", command)
        writeTextFile("geoc_raster_getsize_command_output", result)
    }

}
