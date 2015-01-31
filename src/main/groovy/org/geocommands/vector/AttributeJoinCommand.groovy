package org.geocommands.vector

import au.com.bytecode.opencsv.CSVReader
import geoscript.feature.Feature
import geoscript.feature.Field
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.geotools.data.shapefile.dbf.DbaseFileHeader
import org.geotools.data.shapefile.dbf.DbaseFileReader
import org.kohsuke.args4j.Option

import java.nio.charset.Charset

/**
 * Perform a attribute join between a Layers and a table.
 * @author Jared Erickson
 */
class AttributeJoinCommand extends LayerInOutCommand<AttributeOptions> {

    @Override
    String getName() {
        "vector join attribute"
    }

    @Override
    String getDescription() {
        "Perform a attribute join between a Layers and a table."
    }

    @Override
    AttributeOptions getOptions() {
        new AttributeOptions()
    }

    @Override
    void processLayers(Layer inLayer, Layer outLayer, AttributeOptions options, Reader reader, Writer writer) throws Exception {
        Field layerField = inLayer.schema.get(options.layerFieldName)
        Class typ = Class.forName(Schema.lookUpBinding(layerField.typ))
        Map values = [:]
        Table table = getTable(options)
        List fields = options.fieldNames ?: table.fields*.name
        table.read { Map row ->
            // Cast the key so map lookups work
            Object key = row[options.joinFieldName].asType(typ)
            values[key] = fields.inject([:]) { Map data, String name ->
                data << [(name): row[name]]
            }
        }
        outLayer.withWriter { geoscript.layer.Writer w ->
            inLayer.eachFeature { Feature f ->
                Feature newFeature = w.newFeature
                Map newValues = f.attributes
                Map joinValues = values[f[options.layerFieldName]]
                if (joinValues) {
                    newValues.putAll(joinValues)
                }
                newFeature.set(newValues)
                if (!options.onlyIncludeMatching || joinValues) {
                    w.add(newFeature)
                }
            }
        }
    }

    @Override
    protected Schema createOutputSchema(Layer inputLayer, AttributeOptions options) {
        Table table = getTable(options)
        List fields = table.fields
        if (options.fieldNames) {
            fields = fields.findAll { Field f ->
                f.name in options.fieldNames
            }
        }
        inputLayer.schema.addFields(fields, getOutputLayerName(inputLayer, name, options))
    }

    protected Table getTable(AttributeOptions options) {
        if (options.joinSource.endsWith(".csv")) {
            new CSV(new File(options.joinSource), options.options.get("separator", ","), options.options.get("quote", '"'))
        } else if (options.joinSource.endsWith(".dbf")) {
            new DBF(new File(options.joinSource), options.options.get("encoding", "ISO-8859-1"))
        } else {
            throw IllegalArgumentException("Unknown join source! Only CSV and DBF tables are supported!")
        }
    }

    private static interface Table {

        List<Field> getFields()

        void read(Closure c)

    }

    private static class DBF implements Table {

        private File file;

        private String encoding

        private List<Field> flds = []

        DBF(File file, String encoding) {
            this.file = file;
            this.encoding = encoding
        }

        private void withDbaseFileReader(File file, Closure c) {
            FileInputStream fis = new FileInputStream(file)
            try {
                DbaseFileReader reader = new DbaseFileReader(fis.channel, false, Charset.forName(encoding))
                try {
                    c.call(reader)
                } finally {
                    reader.close()
                }
            } finally {
                fis.close()
            }
        }

        List<Field> getFields() {
            if (flds.isEmpty()) {
                withDbaseFileReader(file) { DbaseFileReader reader ->
                    DbaseFileHeader header = reader.header
                    (0..<header.numFields).each { int i ->
                        flds.add(new Field(header.getFieldName(i), header.getFieldClass(i).name))
                    }
                }
            }
            flds
        }

        void read(Closure c) {
            getFields()
            withDbaseFileReader(file) { DbaseFileReader reader ->
                while (reader.hasNext()) {
                    List values = reader.readEntry() as List
                    Map valueMap = [:]
                    (0..<flds.size()).each { int i ->
                        valueMap[flds.get(i).name] = values[i]
                    }
                    c.call(valueMap)
                }
            }
        }

    }

    private static class CSV implements Table {

        private File file

        private List<Field> flds = []

        private char separator = ','

        private char quote = '"'

        CSV(File file, String separator, String quote) {
            this.file = file
            this.separator = separator
            this.quote = quote
        }

        @Override
        List<Field> getFields() {
            if (flds.isEmpty()) {
                CSVReader reader = new CSVReader(new FileReader(file), separator, quote)
                try {
                    List cols = reader.readNext() as List
                    flds = cols.collect { String name ->
                        String type = "String"
                        // Try to extract type (name:type)
                        if (name.contains(":")) {
                            String[] parts = name.split(":")
                            name = parts[0]
                            type = parts[1]
                        }
                        new Field(name, type)
                    }
                } finally {
                    reader.close()
                }
            }
            flds
        }

        @Override
        void read(Closure c) {
            getFields()
            CSVReader reader = new CSVReader(new FileReader(file), separator, quote)
            try {
                // Skip Column names
                reader.readNext()
                // Read values
                def values
                while ((values = reader.readNext()) != null) {
                    // Skip blank lines
                    if (values.length > 0 && values[0] != null) {
                        Map valueMap = [:]
                        (0..<flds.size()).each { int i ->
                            valueMap[flds.get(i).name] = values[i]
                        }
                        c.call(valueMap)
                    }
                }
            } finally {
                reader.close()
            }
        }

    }

    static class AttributeOptions extends LayerInOutOptions {

        @Option(name = "-s", aliases = "--table-source", usage = "The table source", required = true)
        String joinSource

        @Option(name = "-t", aliases = "--table-name", usage = "The table name", required = false)
        String joinName

        @Option(name = "-y", aliases = "--layer-field", usage = "The input layer field name", required = true)
        String layerFieldName

        @Option(name = "-j", aliases = "--table-field", usage = "The other layer field name", required = true)
        String joinFieldName

        @Option(name = "-n", aliases = "--field", usage = "The join field names to include in the output layer", required = false)
        List<String> fieldNames = []

        @Option(name = "-m", aliases = "--only-include-matching", usage = "The flag to whether only include matching rows", required = false)
        boolean onlyIncludeMatching = false

        @Option(name = "-p", aliases = "--options", usage = "The options (for csv separator and quote, for dbf encoding)", required = false)
        Map<String, String> options = [:]
    }
}
