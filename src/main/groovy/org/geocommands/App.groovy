package org.geocommands

import geoscript.workspace.OGR
import org.geotools.data.ogr.OGRDataStoreFactory
import org.geotools.util.logging.Logging
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

/**
 * The Command line "geoc" application.
 * @author Jared Erickson
 */
class App {

    static void main(String[] args) {

        // Set XY coordinate ordering
        System.setProperty("org.geotools.referencing.forceXY", "true")

        // Turn off logging
        LogManager.getLogManager().reset()
        Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
        globalLogger.setLevel(Level.OFF)
        Logging.getLogger(OGRDataStoreFactory.class).setLevel(Level.OFF)

        // Turn off GDAL logging
        if (OGR.isAvailable()) {
            OGR.setErrorHandler("quiet")
        }

        try {
            Commands.execute(
                    args,
                    new InputStreamReader(System.in),
                    new OutputStreamWriter(System.out),
                    new OutputStreamWriter(System.err)
            );
        } catch (Commands.CommandException e) {
            System.exit(-1)
        }
    }
}
