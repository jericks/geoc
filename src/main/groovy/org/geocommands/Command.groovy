package org.geocommands

abstract class Command<T extends Options>  {
    abstract String getName()
    abstract String getDescription()
    abstract T getOptions()
    abstract void execute(T options, Reader reader, Writer writer) throws Exception
    String execute(T options, String input) throws Exception {
        execute(options, new StringReader(input))
    }
    String execute(T options, Reader reader) throws Exception {
        StringWriter writer = new StringWriter()
        execute(options, reader, writer)
        writer.toString()
    }
    String execute(T options) throws Exception {
        execute(options, new StringReader(""))
    }
    static {
        System.setProperty("org.geotools.referencing.forceXY", "true")
    }
}
