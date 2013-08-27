package org.geocommands.vector

import org.kohsuke.args4j.Option

/**
 * Options base class that contains two input Layers and an output Layer
 * @author Jared Erickson
 */
class LayerInOtherOutOptions extends LayerInOutOptions {

    @Option(name="-k", aliases="--other-workspace",  usage="The other workspace", required = true)
    String otherWorkspace

    @Option(name="-y", aliases="--other-layer",  usage="The other layer", required = false)
    String otherLayer

}
