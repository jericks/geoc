package org.geocommands.vector

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import groovy.transform.CompileStatic
import org.geocommands.App
import org.geocommands.vector.RandomPointsCommand.RandomPointsOptions
import org.geocommands.vector.BufferCommand.BufferOptions
import org.geocommands.vector.IntersectsCommand.IntersectsOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.*

@CompileStatic
class IntersectsCommandTest extends BaseTest {

    private File createPoints(String name, int number) {
        File pointsFile = folder.newFile("${name}.shp")
        RandomPointsCommand randomPointsCommand = new RandomPointsCommand()
        RandomPointsOptions options = new RandomPointsOptions()
        options.geometry = "-180,-90,180,90"
        options.number = number
        options.outputWorkspace = pointsFile.absolutePath
        randomPointsCommand.execute(options)
        pointsFile
    }

    private File createPolygons() {
        File polysFile = folder.newFile("polys.shp")
        File pointsFile = createPoints("polys_points", 10)
        BufferCommand bufferCommand = new BufferCommand()
        BufferOptions bufferOptions = new BufferOptions()
        bufferOptions.inputWorkspace = pointsFile.absolutePath
        bufferOptions.outputWorkspace = polysFile.absolutePath
        bufferOptions.distance = 8
        bufferCommand.execute(bufferOptions)
        polysFile
    }

    @Test void execute() {
        File pointsFile = createPoints("points", 1000)
        File polysFile = createPolygons()
        File pointsInPolysFile = createTemporaryShapefile("pointsInPolys")
        IntersectsCommand cmd = new IntersectsCommand()
        IntersectsOptions options = new IntersectsOptions(
                inputWorkspace: pointsFile.absolutePath,
                otherWorkspace: polysFile.absolutePath,
                outputWorkspace: pointsInPolysFile.absolutePath
        )
        cmd.execute(options)
        Layer layer = new Shapefile(pointsInPolysFile)
        assertTrue layer.count > 0
    }

    @Test void run() {
        File pointsFile = createPoints("points", 1000)
        File polysFile = createPolygons()
        File pointsInPolysFile = createTemporaryShapefile("pointsInPolys")
        App.main([
                "vector intersects",
                "-i", pointsFile.absolutePath,
                "-k", polysFile.absolutePath,
                "-o", pointsInPolysFile.absolutePath
        ] as String[])
        Layer layer = new Shapefile(pointsInPolysFile)
        assertTrue layer.count > 0
    }


}
