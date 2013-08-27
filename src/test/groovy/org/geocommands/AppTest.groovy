package org.geocommands

import org.junit.After
import org.junit.Before
import org.junit.Test

import java.security.Permission

import static org.junit.Assert.*

/**
 * The App Unit Test
 * @author Jared Erickson
 */
class AppTest extends BaseTest {

    private static final String  NEW_LINE = System.getProperty("line.separator")

    @Before
    public void before() throws Exception {
        System.setSecurityManager(new OverrideExitSecurityManager())
    }

    @After
    public void after() throws Exception {
        System.setSecurityManager(null)
    }

    @Test void runWithNoCommandName() {
        try {
            String output = runApp([],"")
            assertEquals "Please enter a geocommand!${NEW_LINE}Usage: geoc <command> <args>", output
        } catch (OverrideExitException ex) {
        }
    }

    @Test void runWithUnknownCommandName() {
        try {
            String output = runApp(["asdfsa"],"")
            assertEquals "Unknown geocommand: 'asdfsa'!\"${NEW_LINE}Usage: geoc <command> <args>", output
        } catch (OverrideExitException ex) {
        }
    }

    @Test void runForHelp() {
        try {
            String output = runApp(["vector centroid","--help"],"")
            assertEquals("geoc vector centroid: Get the centroid of each feature in the input Layer and save them to the output Layer" + NEW_LINE
                + " --help                      : Print the help message" + NEW_LINE
                + " -i (--input-workspace) VAL  : The input workspace" + NEW_LINE
                + " -l (--input-layer) VAL      : The input layer" + NEW_LINE
                + " -o (--output-workspace) VAL : The output workspace" + NEW_LINE
                + " -r (--output-layer) VAL     : The output layer" + NEW_LINE, output)
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
