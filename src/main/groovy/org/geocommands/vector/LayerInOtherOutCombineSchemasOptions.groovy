package org.geocommands.vector

import org.kohsuke.args4j.Option

/**
 * A LayerInOtherOutOptions subclass for LayerInOutOtherCombineSchemaCommands that combine Schema from the input and other Layers.
 * @author Jared Erickson
 */
class LayerInOtherOutCombineSchemasOptions extends LayerInOtherOutOptions {

    List fields

    @Option(name = "-p", aliases = "--postfix-all", usage = "Whether to postfix all field names (true) or not (false). If true, all Fields from the this current Schema will have '1' at the end of their name while the other Schema's Fields will have '2'.", required = false)
    boolean postfixAll = false

    @Option(name = "-d", aliases = "--include-duplicates", usage = "Whether or not to include duplicate fields names. Defaults to false. If a duplicate is found a '2' will be added.", required = false)
    boolean includeDuplicates = false

    @Option(name = "-m", aliases = "--maxfieldname-length", usage = "The maximum new Field name length (mostly to support shapefiles where Field names can't be longer than 10 characters", required = false)
    int maxFieldNameLength = -1

    @Option(name = "-f", aliases = "--first-postfix", usage = "The postfix for fields from the first Layer", required = false)
    String firstPostfix = "1"

    @Option(name = "-s", aliases = "--second-postfix", usage = "The postfix for fields from the second Layer", required = false)
    String secondPostfix = "2"

}
