package org.geocommands.vector

import org.kohsuke.args4j.Option

/**
 * An Options subclass for Commands that read and write Layers
 * @author Jared Erickson
 */
class LayerInOutOptions extends LayerOptions {

    @Option(name = "-o", aliases = "--output-workspace", usage = "The output workspace", required = false)
    String outputWorkspace

    @Option(name = "-r", aliases = "--output-layer", usage = "The output layer", required = false)
    String outputLayer
}
