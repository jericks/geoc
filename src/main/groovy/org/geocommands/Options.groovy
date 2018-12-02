package org.geocommands

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.Option

/**
 * The base Options class that holds the name(s) of the command
 * and a help flag.
 * @author Jared Erickson
 */
class Options {

    @Argument(index = 0, required = true)
    List<String> names = []

    @Option(name = "--help", usage = "Print the help message", required = false)
    boolean help

    @Option(name = "--web-help", usage = "Open help in a browser", required = false)
    boolean webHelp

    String getName() {
        names.join(" ")
    }

}
