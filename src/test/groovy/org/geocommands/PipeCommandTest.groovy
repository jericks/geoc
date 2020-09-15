package org.geocommands

import geoscript.layer.Shapefile
import org.junit.Test

class PipeCommandTest extends BaseTest {

    @Test
    void execute() {
        File shpFile = createTemporaryShapefile("piperesult")
        PipeCommand cmd = new PipeCommand()
        PipeCommand.PipeOptions options = new PipeCommand.PipeOptions(
                commands: "vector randompoints -g 0,0,10,10 -n 10 | vector buffer -d 2 -o ${shpFile.absolutePath}"
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 10, shp.count
        org.junit.Assert.assertEquals "MultiPolygon", shp.schema.geom.typ
    }

    @Test
    void runAsCommandLine() {
        File shpFile = createTemporaryShapefile("randompoints")
        App.main([
                "pipe",
                "-c", "vector randompoints -g 0,0,10,10 -n 10 | vector buffer -d 2 -o ${shpFile.absolutePath}"
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        org.junit.Assert.assertEquals 10, shp.count
        org.junit.Assert.assertEquals "MultiPolygon", shp.schema.geom.typ
    }
    
}
