package org.geocommands.vector

import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Layer output Options
 * @author Jared Erickson
 */
class LayerOutOptions extends Options {

    @Option(name="-o", aliases="--output-workspace",  usage="The output workspace", required = false)
    String outputWorkspace

    @Option(name="-r", aliases="--output-layer",  usage="The output layer", required = false)
    String outputLayer
}
