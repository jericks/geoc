package org.geocommands.filter

import geoscript.filter.Filter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

/**
 * Convert a CQL statement to an OCG XML Filter
 * @author Jared Erickson
 */
class CqlToXmlCommand extends Command<CqlToXmlOptions> {

    @Override
    String getName() {
        "filter cql2xml"
    }

    @Override
    String getDescription() {
        "Convert a CQL statement to an OCG XML Filter"
    }

    @Override
    CqlToXmlOptions getOptions() {
        new CqlToXmlOptions()
    }

    @Override
    void execute(CqlToXmlOptions options, Reader reader, Writer writer) throws Exception {
        Filter filter = new Filter(options.cql ? options.cql : reader.text)
        writer.write(filter.xml)
    }

    static class CqlToXmlOptions extends Options {

        @Option(name = "-c", aliases = "--cql", usage = "The CQL statement", required = false)
        String cql

    }

}
