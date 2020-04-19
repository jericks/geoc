package org.geocommands

import org.junit.Test
import org.kohsuke.args4j.Option

import java.text.SimpleDateFormat

class ManPagesTest {

    static String NEW_LINE = System.getProperty("line.separator")

    @Test
    void createManPages() {

        File dir = new File("../geoc/src/man")

        def examples = new Properties()
        new File("src/test/resources/example.properties").withInputStream { inp ->
            examples.load(inp)
        }

        String date = new SimpleDateFormat("d MMMM yyyy").format(new Date())

        String version = "0.1"

        boolean overwrite = false

        ServiceLoader.load(Command.class).each { cmd ->
            String cmdName = cmd.name.replaceAll(" ", "-")
            def file = new File(dir, "geoc-${cmdName}.1")

            String oldContents = ""
            if (file.exists()) {
                oldContents = file.text
            }

            String contents = ".TH \"geoc-${cmdName}\" \"1\" \"${date}\" \"version ${version}\"" + NEW_LINE
            contents += ".SH NAME" + NEW_LINE
            contents += "geoc ${cmd.name}" + NEW_LINE
            contents += ".SH DESCRIPTION" + NEW_LINE
            contents += "${cmd.description}" + NEW_LINE
            String example = examples.getProperty("geoc-${cmdName.toLowerCase()}")
            if (example) {
                contents += ".SH USAGE" + NEW_LINE
                contents += "${example}" + NEW_LINE
            }

            contents += ".SH OPTIONS" + NEW_LINE

            def options = cmd.options
            for (def c = options.getClass(); c != null; c = c.superclass) {
                c.declaredFields.each { fld ->
                    def opt = fld.getAnnotation(Option)
                    if (opt) {
                        contents += "${opt.name()} ${opt.aliases().join(' ')}: ${opt.usage()}" + NEW_LINE
                        contents += ".PP" + NEW_LINE
                    }
                }
            }

            if (!removeDate(contents).equals(removeDate(oldContents))) {
                println "Man page for ${cmd.name} at ${file}"
                file.write(contents)
            }

        }

    }

    static String removeDate(String content) {
        List<String> lines = content.split(NEW_LINE)
        String firstLine = lines[0].replaceFirst('\\d+ \\w+ \\d\\d\\d\\d', '')
        firstLine + (1..<lines.size()).collect { lines[it] }.join(NEW_LINE)
    }

}
