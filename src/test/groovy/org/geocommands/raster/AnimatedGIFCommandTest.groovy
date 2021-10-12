package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.AnimatedGIFCommand.AnimatedGIFOptions
import org.junit.jupiter.api.Test

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.util.List

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * The AnimatedGIFCommand Unit Test
 * @author Jared Erickson
 */
class AnimatedGIFCommandTest extends BaseTest {

    protected List<File> createImageFiles() {
        List files = [
                new File(folder, "image1.gif"),
                new File(folder, "image2.gif"),
                new File(folder, "image3.gif")
        ]
        files.each { File f ->
            BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB)
            Graphics graphics = image.graphics
            graphics.paint = geoscript.filter.Color.getRandom().asColor()
            graphics.fillRect(0, 0, 256, 256)
            graphics.dispose()
            ImageIO.write(image, "gif", f)
        }
        files
    }

    @Test
    void execute() {

        File outputFile = new File(folder, "animated.gif")
        List files = createImageFiles()

        AnimatedGIFOptions options = new AnimatedGIFOptions(
                files: files,
                delay: 500,
                repeat: true,
                file: outputFile
        )
        AnimatedGIFCommand command = new AnimatedGIFCommand()
        command.execute(options)

        assertTrue outputFile.exists()
        assertTrue outputFile.length() > 0
    }

    @Test
    void run() {

        File outputFile = new File(folder, "animated.gif")
        List files = createImageFiles()

        runApp([
                "raster animatedgif",
                "-f", files[0].absolutePath,
                "-f", files[1].absolutePath,
                "-f", files[2].absolutePath,
                "-o", outputFile.absolutePath,
                "-d", 450,
                "-r"
        ], "")

        assertTrue outputFile.exists()
        assertTrue outputFile.length() > 0
    }

}
