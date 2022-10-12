package org.geocommands.doc

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
        fromFile.withInputStream {InputStream inputStream ->
            toFile.withOutputStream { OutputStream outputStream ->
                outputStream << inputStream
            }
        }
    }

    List<Map> getCommandOptions(Command cmd) {
        def processOptions = { Class clazz, List<Map> options ->
            clazz.getDeclaredFields().each { Field fld ->
                Annotation annotation = fld.getAnnotation(Option)
                if (annotation) {
                    Option opt = annotation as Option
                    String name = opt.name()
                    List aliases = opt.aliases()
                    String alias = aliases.isEmpty() ? "" : aliases.first()
                    String usage = opt.usage()
                    if (name.startsWith("--")) {
                        alias = name
                        name = ""
                    }
                    options.add([name: name, alias: alias, usage: usage])
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

}
