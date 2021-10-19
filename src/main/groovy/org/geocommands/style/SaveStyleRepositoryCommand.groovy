package org.geocommands.style

import geoscript.style.StyleRepository
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class SaveStyleRepositoryCommand extends Command<SaveStyleRepositoryOptions> {

    @Override
    String getName() {
        "style repository save"
    }

    @Override
    String getDescription() {
        "Save a style to a style repository"
    }

    @Override
    SaveStyleRepositoryOptions getOptions() {
        new SaveStyleRepositoryOptions()
    }

    @Override
    void execute(SaveStyleRepositoryOptions options, Reader reader, Writer writer) throws Exception {
        StyleRepository styleRepository = StyleRepositoryFactory.getStyleRepository(options.type, options.params)
        styleRepository.save(options.layerName, options.styleName ?: options.layerName, options.styleFile ? options.styleFile.text : reader.text)
    }

    static class SaveStyleRepositoryOptions extends Options {

        @Option(name = "-t", aliases = "--type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String type = "directory"

        @Option(name = "-o", aliases = "--options", usage = "The style repository options", required = false)
        Map params = [:]

        @Option(name = "-l", aliases = "--layer-name", usage = "The layer name", required = true)
        String layerName

        @Option(name = "-s", aliases = "--style-name", usage = "The style name", required = false)
        String styleName

        @Option(name = "-f", aliases = "--style-file", usage = "The style file (sld or css)", required = false)
        File styleFile

    }

}
