package org.geocommands

class Commands {

    static List<Command> list() {
        ServiceLoader.load(Command).iterator().collect()
    }
    
    static Command find(String name) {
        list().find{ Command command ->
            command.name.equalsIgnoreCase(name)
        }
    }

    static void execute(String name, Map<String,Object> properties, Reader reader, Writer writer) {
        Command command = find(name)
        Options options = command.options
        properties.each { String key, Object value ->
            if (options.hasProperty(key)) {
                options.setProperty(key, value)
            }
        }
        command.execute(options, reader, writer)
    }

    static String execute(String name, Map<String,Object> properties) {
        StringReader reader = new StringReader("")
        StringWriter writer = new StringWriter()
        execute(name, properties, reader, writer)
        writer.toString()
    }

}
