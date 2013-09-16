package org.geocommands.vector

import org.geocommands.Command
import org.geocommands.Options
import org.geotools.data.DataStoreFinder
import org.kohsuke.args4j.Option

/**
 * List all parameters for the given DataStore
 * @author Jared Erickson
 */
class DataStoreParametersCommand extends Command<DataStoreParametersOptions> {

    String getName() {
        "vector datastoreparams"
    }

    String getDescription() {
        "List all parameters for the given DataStore"
    }

    DataStoreParametersOptions getOptions() {
        new DataStoreParametersOptions()
    }

    void execute(DataStoreParametersOptions options, Reader reader, Writer writer) {

        def ds = DataStoreFinder.availableDataStores.find{ds ->
            if (ds.displayName.equalsIgnoreCase(options.name)) {
                return ds
            }
        }

        String NEW_LINE = System.getProperty("line.separator")
        StringBuilder builder = new StringBuilder()
        ds.parametersInfo.eachWithIndex{param, i ->
            if (i > 0) {
                builder.append(NEW_LINE)
            }
            builder.append(param.name)
        }
        writer.write(builder.toString())
    }

    private static class DataStoreParametersOptions extends Options {

        @Option(name="-n", aliases="--name",  usage="The DataStore name", required = true)
        String name

    }
}
