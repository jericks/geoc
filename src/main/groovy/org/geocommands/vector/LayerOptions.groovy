package org.geocommands.vector

import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * An Options subclass for Commands that read in a Layers
 * @author Jared Erickson
 */
class LayerOptions extends Options {

    @Option(name = "-i", aliases = "--input-workspace", usage = "The input workspace", required = false)
    String inputWorkspace

    @Option(name = "-l", aliases = "--input-layer", usage = "The input layer", required = false)
    String inputLayer

}
