package org.geocommands

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.Permission

import static org.junit.jupiter.api.Assertions.*

/**
 * The App Unit Test
 * @author Jared Erickson
 */
class AppTest extends BaseTest {

    @BeforeEach
    public void before() throws Exception {
        System.setSecurityManager(new OverrideExitSecurityManager())
    }

    @AfterEach
    public void after() throws Exception {
        System.setSecurityManager(null)
    }

    @Test
    void runWithNoCommandName() {
        try {
            String output = runApp([], "")
            assertEquals "Please enter a geocommand!${NEW_LINE}Usage: geoc <command> <args>", output
        } catch (OverrideExitException ex) {
        }
    }

    @Test
    void runWithUnknownCommandName() {
        try {
            String output = runApp(["asdfsa"], "")
            assertEquals "Unknown geocommand: 'asdfsa'!\"${NEW_LINE}Usage: geoc <command> <args>", output
        } catch (OverrideExitException ex) {
        }
    }

    @Test
    void runForHelp() {
        try {
            String output = runApp(["vector centroid", "--help"], "")
            assertEquals("geoc vector centroid: Get the centroid of each feature in the input Layer and save them to the output Layer" + NEW_LINE +
                    " --help                      : Print the help message (default: true)" + NEW_LINE +
                    " --web-help                  : Open help in a browser (default: false)" + NEW_LINE +
                    " -i (--input-workspace) VAL  : The input workspace" + NEW_LINE +
                    " -l (--input-layer) VAL      : The input layer" + NEW_LINE +
                    " -o (--output-workspace) VAL : The output workspace" + NEW_LINE +
                    " -r (--output-layer) VAL     : The output layer" + NEW_LINE, output)
        } catch (OverrideExitException ex) {
        }
    }

    @Test
    void runWithError() {
        try {
            String output = runApp(["vector centroid", "-i", "BAD_INPUT", "-o", "BAD_OUTPUT"], "")
            assertTrue(output.contains("Unknown Workspace parameter string: BAD_INPUT" + NEW_LINE +
                    "Usage: geoc <command> <args>" + NEW_LINE +
                    " --help                      : Print the help message (default: false)" + NEW_LINE +
                    " --web-help                  : Open help in a browser (default: false)" + NEW_LINE +
                    " -i (--input-workspace) VAL  : The input workspace (default: BAD_INPUT)" + NEW_LINE +
                    " -l (--input-layer) VAL      : The input layer" + NEW_LINE +
                    " -o (--output-workspace) VAL : The output workspace (default: BAD_OUTPUT)" + NEW_LINE +
                    " -r (--output-layer) VAL     : The output layer" + NEW_LINE))
        } catch (OverrideExitException ex) {
        }
    }

    @Test
    void runForHelpWithRequiredParams() {
        try {
            String output = runApp(["geometry greatcirclearc", "--help"], "")
            assertEquals("geoc geometry greatcirclearc: Create a great circle arc." + NEW_LINE +
                    " --help                 : Print the help message (default: true)" + NEW_LINE +
                    " --web-help             : Open help in a browser (default: false)" + NEW_LINE +
                    " -e (--ellipsoid) VAL   : The ellipsoid (default: wgs84)" + NEW_LINE +
                    " -n (--num-points) N    : The number of points (default: 100)" + NEW_LINE +
                    " -p (--start-point) VAL : The start point" + NEW_LINE +
                    " -t (--end-point) VAL   : The end point" + NEW_LINE, output)
        } catch (OverrideExitException ex) {
        }
    }

    protected static class OverrideExitException extends SecurityException {

        final int status

        public OverrideExitException(int status) {
            super("Override exit")
            this.status = status
        }
    }

    protected static class OverrideExitSecurityManager extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status)
            throw new OverrideExitException(status)
        }
    }

}
