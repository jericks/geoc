package org.geocommands.style


import geoscript.style.StyleRepository
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class DeleteStyleRepositoryCommand extends Command<DeleteStyleRepositoryOptions> {

    @Override
    String getName() {
        "style repository delete"
    }

    @Override
    String getDescription() {
        "Delete a style to a style repository"
    }

    @Override
    DeleteStyleRepositoryOptions getOptions() {
        new DeleteStyleRepositoryOptions()
    }

    @Override
    void execute(DeleteStyleRepositoryOptions options, Reader reader, Writer writer) throws Exception {
        StyleRepository styleRepository = StyleRepositoryFactory.getStyleRepository(options.type, options.params)
        styleRepository.delete(options.layerName, options.styleName ?: options.layerName)
    }

    static class DeleteStyleRepositoryOptions extends Options {

        @Option(name = "-t", aliases = "--type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String type = "directory"

        @Option(name = "-o", aliases = "--options", usage = "The style repository options", required = false)
        Map params = [:]

        @Option(name = "-l", aliases = "--layer-name", usage = "The layer name", required = true)
        String layerName

        @Option(name = "-s", aliases = "--style-name", usage = "The style name", required = false)
        String styleName
        
    }

}
