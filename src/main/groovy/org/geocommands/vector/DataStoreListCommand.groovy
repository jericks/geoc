package org.geocommands.vector

import org.geocommands.Command
import org.geocommands.Options
import org.geotools.data.DataStoreFinder

/**
 * Get a list of available DataStores
 * @author Jared Erickson
 */
class DataStoreListCommand extends Command<DataStoreListOptions> {

    String getName() {
        "vector datastorelist"
    }

    String getDescription() {
        "List all available DataStores"
    }

    DataStoreListOptions getOptions() {
        new DataStoreListOptions()
    }

    void execute(DataStoreListOptions options, Reader reader, Writer writer) {
        String NEW_LINE = System.getProperty("line.separator")
        StringBuilder builder = new StringBuilder()
        DataStoreFinder.availableDataStores.eachWithIndex { ds, i ->
            if (i > 0) {
                builder.append(NEW_LINE)
            }
            builder.append(ds.displayName)
        }
        writer.write(builder.toString())
    }

    static class DataStoreListOptions extends Options {
    }
}
