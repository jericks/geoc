package org.geocommands

/**
 * The Abstract class for all Commands
 * @param < T >  The Options
 */
abstract class Command<T extends Options> {

    /**
     * Get the name of the command
     * @return The name of the command
     */
    abstract String getName()

    /**
     * Get the short description
     * @return A short description
     */
    abstract String getDescription()

    /**
     * Get the Options
     * @return The Options
     */
    abstract T getOptions()

    /**
     * Execute the command
     * @param options The Options
     * @param reader The Reader
     * @param writer The Write
     * @throws Exception if an error occurs
     */
    abstract void execute(T options, Reader reader, Writer writer) throws Exception

    /**
     * Execute the Command and return a String
     * @param options The Options
     * @param input The input String
     * @return The output String
     * @throws Exception if an error occurs
     */
    String execute(T options, String input) throws Exception {
        execute(options, new StringReader(input))
    }

    /**
     * Execute the Command and return a String
     * @param options The Options
     * @param reader A input Reader
     * @return The output String
     * @throws Exception if an error occurs
     */
    String execute(T options, Reader reader) throws Exception {
        StringWriter writer = new StringWriter()
        execute(options, reader, writer)
        writer.toString()
    }

    /**
     * Execute the Command and return a String
     * @param options The Options
     * @return The output String
     * @throws Exception if an error occurs
     */
    String execute(T options) throws Exception {
        execute(options, new StringReader(""))
    }

    /**
     * Set the GeoTools forceXY property to true
     */
    static {
        System.setProperty("org.geotools.referencing.forceXY", "true")
    }
}
