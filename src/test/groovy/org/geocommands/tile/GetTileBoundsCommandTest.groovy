package org.geocommands.tile

import org.geocommands.tile.GetTileBoundsCommand.GetTileBoundsOptions
import org.geocommands.BaseTest
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertEquals

class GetTileBoundsCommandTest extends BaseTest {

    @Test void execute() {
        GetTileBoundsCommand cmd = new GetTileBoundsCommand()
        GetTileBoundsOptions options = new GetTileBoundsOptions(
            pyramid: "mercator",
            z: 10,
            x: 525,
            y: 345
        )
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String result = writer.toString()
        assertEquals "POLYGON ((508736.5955516733 -6535659.55323807, 508736.5955516733 -6496523.867290536, 547870.179824879 -6496523.867290536, 547870.179824879 -6535659.55323807, 508736.5955516733 -6535659.55323807))", result
    }

    @Test void run() {
        String result = runApp([
            "tile get bounds",
            "-z", "10",
            "-x", "525",
            "-y", "345"
        ], "mercator")
        assertEquals "POLYGON ((508736.5955516733 -6535659.55323807, 508736.5955516733 -6496523.867290536, 547870.179824879 -6496523.867290536, 547870.179824879 -6535659.55323807, 508736.5955516733 -6535659.55323807))" + NEW_LINE, result
    }

}
