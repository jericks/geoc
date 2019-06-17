package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import org.geocommands.App
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*
import org.geocommands.vector.PointsAlongLineCommand.PointsAlongLineOptions

class PointsAlongLineCommandTest extends BaseTest {

    @Test
    void executeWithFiles() {
        File file = getResource("lines.properties")
        File shpFile = createTemporaryShapefile("points")
        PointsAlongLineCommand cmd = new PointsAlongLineCommand()
        PointsAlongLineOptions options = new PointsAlongLineOptions(
            inputWorkspace: file.absolutePath,
            outputWorkspace: shpFile,
            distance: 1
        )
        cmd.execute(options, new StringReader(""), new StringWriter())
        Layer layer = new Shapefile(shpFile)
        assertEquals 20, layer.count
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("lines.properties")
        File shpFile = createTemporaryShapefile("points")
        App.main([
                "vector pointsalongline",
                "-i", file.absolutePath,
                "-o", shpFile.absolutePath,
                "-d", 2
        ] as String[])
        Shapefile shp = new Shapefile(shpFile)
        assertEquals 10, shp.count
        assertEquals "Point", shp.schema.geom.typ
    }
}
