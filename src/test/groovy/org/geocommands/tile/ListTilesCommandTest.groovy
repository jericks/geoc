package org.geocommands.tile

import org.geocommands.tile.ListTilesCommand.ListTilesOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals


class ListTilesCommandTest extends BaseTest {

    @Test void execute() {
        ListTilesCommand cmd = new ListTilesCommand()
        ListTilesOptions options = new ListTilesOptions(
            pyramid: "mercator",
            bounds: "2315277.538707974,4356146.199006655,2534193.2172859586,4470343.227121928",
            z: 10
        )
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String result = writer.toString()
        assertEquals """10/571/623
10/572/623
10/573/623
10/574/623
10/575/623
10/576/623
10/571/624
10/572/624
10/573/624
10/574/624
10/575/624
10/576/624
10/571/625
10/572/625
10/573/625
10/574/625
10/575/625
10/576/625
10/571/626
10/572/626
10/573/626
10/574/626
10/575/626
10/576/626
""".denormalize(), result
    }

    @Test void run() {
        String result = runApp([
                "tile list tiles",
                "-b", "2315277.538707974,4356146.199006655,2534193.2172859586,4470343.227121928",
                "-z", "10"
        ], "mercator")
        assertEquals """10/571/623
10/572/623
10/573/623
10/574/623
10/575/623
10/576/623
10/571/624
10/572/624
10/573/624
10/574/624
10/575/624
10/576/624
10/571/625
10/572/625
10/573/625
10/574/625
10/575/625
10/576/625
10/571/626
10/572/626
10/573/626
10/574/626
10/575/626
10/576/626

""".denormalize(), result
    }

}
