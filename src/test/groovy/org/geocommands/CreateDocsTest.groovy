package org.geocommands

import org.junit.Test
import org.kohsuke.args4j.Option

class CreateDocsTest {

    private final static String NEW_LINE = System.getProperty("line.separator")

    @Test
    void createDocs() {

        File dir = new File("src/docs")
        if (!dir.exists()) {
            dir.mkdir()
        }

        def examples = new Properties()
        new File("src/test/resources/example.properties").withInputStream { inp ->
            examples.load(inp)
        }

        ServiceLoader.load(Command.class).each { cmd ->
            String cmdName = cmd.name.replaceAll(" ", "-")
            File file = new File(dir, "${cmdName}.md")
            String example = examples.getProperty("geoc-${cmdName.toLowerCase()}")

            String optionStr = ""
            def options = cmd.options
            for (def c = options.getClass(); c != null; c = c.superclass) {
                c.declaredFields.each { fld ->
                    def opt = fld.getAnnotation(Option.class)
                    if (opt) {
                        optionStr += "   * ${opt.name()} ${opt.aliases().join(' ')}: ${opt.usage()}" + NEW_LINE * 2
                    }
                }
            }
            file.write("""
${cmd.name}
${"=" * cmd.name.length()}

**Name**:

geoc ${cmd.name}

**Description**:

${cmd.description}

**Arguments**:

${optionStr}

${example ? '**Example**::' : ''}

    ${example ? example : ''}


""".trim())

        }

    }

}
