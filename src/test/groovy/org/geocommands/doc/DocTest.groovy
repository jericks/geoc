package org.geocommands.doc

import geoscript.feature.Feature
import geoscript.feature.Schema
import geoscript.layer.Layer
import org.geocommands.BaseTest
import org.geocommands.Command
import org.kohsuke.args4j.Option

import java.lang.annotation.Annotation
import java.lang.reflect.Field

class DocTest extends BaseTest {

    String runApp(String args, String input="") {
        runApp(args.split(" ").tail() as List, input)
    }

    void writeTextFile(String name, String contents) {
        File dir = new File("src/main/docs/output")
        if (!dir.exists()) {
            dir.mkdir()
        }
        File file = new File(dir, "${name}.txt")
        file.text = contents
    }

    File copyFile(File fromFile, File toFile) {
        fromFile.withInputStream { InputStream inputStream ->
            toFile.withOutputStream { OutputStream outputStream ->
                outputStream << inputStream
            }
        }
        toFile
    }

    List<Map> getCommandOptions(Command cmd) {
        def processOptions = { Class clazz, List<Map> options ->
            clazz.getDeclaredFields().each { Field fld ->
                Annotation annotation = fld.getAnnotation(Option)
                if (annotation) {
                    Option opt = annotation as Option
                    String name = opt.name()
                    List aliases = opt.aliases()
                    boolean isRequired = opt.required()
                    String alias = aliases.isEmpty() ? "" : aliases.first()
                    String usage = opt.usage()
                    if (name.startsWith("--")) {
                        alias = name
                        name = ""
                    }
                    options.add([name: name, alias: alias, usage: usage, required: isRequired])
                }
            }
        }
        List<Map> options = []
        Class clazz = cmd.getOptions().class
        while(clazz) {
            processOptions(clazz, options)
            clazz = clazz.superclass
        }
        options
    }

    String createOptionTable(Command cmd) {
        List<Map> options = getCommandOptions(cmd)
        String str = ""
        str += "|===\n"
        str += "| Short Name | Long Name | Description \n\n"
        options.each { Map option ->
            str += "| ${option.name} | ${option.alias} | ${option.usage}\n"
        }
        str += "|==="
        str
    }

    String createSchemaTable(Schema schema, List fieldNames = []) {
        List fields = fieldNames.isEmpty() ? schema.fields : schema.fields.findAll { fieldNames.contains(it.name)}
        String str = ""
        str += "|===\n"
        str += "| Name | Type \n\n"
        fields.each { geoscript.feature.Field field ->
            str += "| ${field.name} | ${field.typ}\n"
        }
        str += "|==="
        str
    }

    String createFeatureTable(Layer layer, List fieldNames = [], int numberOfFeatures = -1) {
        List fields = fieldNames.isEmpty() ? layer.schema.fields : layer.schema.fields.findAll { fieldNames.contains(it.name)}
        int
        String str = ""
        str += "|===\n"
        str += "| ${fields.collect { it.name }.join("|")} \n\n"
        if (numberOfFeatures == -1) {
            layer.eachFeature { Feature feature ->
                str += "| ${fields.collect { feature[it.name] }.join("|")}\n"
            }
        } else {
            layer.features.take(numberOfFeatures).each { Feature feature ->
                str += "| ${fields.collect { feature[it.name] }.join("|")}\n"
            }
        }
        str += "|==="
        str
    }

}
