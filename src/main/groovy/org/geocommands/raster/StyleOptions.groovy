package org.geocommands.raster

import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * A Options base class for Style Commands
 * @author Jared Erickson
 */
class StyleOptions extends Options {

    @Option(name = "-o", aliases = "--opacity", usage = "The opacity", required = false)
    String opacity = "1.0"

}
