package org.geocommands

class VersionCommand extends Command<VersionOptions> {

    String getName() {
        "version"
    }

    String getDescription() {
        "Get the version"
    }

    VersionOptions getOptions() {
        new VersionOptions()
    }

    void execute(VersionOptions options, Reader reader, Writer writer) {
        VersionCommand.class.getClassLoader().getResource("application.properties").withInputStream { InputStream inputStream ->
            Properties properties = new Properties()
            properties.load(inputStream)
            writer.write(properties.getProperty("version"))
        }
    }

    static class VersionOptions extends Options {
    }

}
