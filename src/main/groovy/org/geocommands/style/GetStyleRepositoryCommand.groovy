package org.geocommands.style


import geoscript.style.StyleRepository
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class GetStyleRepositoryCommand extends Command<GetStyleRepositoryOptions> {

    @Override
    String getName() {
        "style repository get"
    }

    @Override
    String getDescription() {
        "Get a styles from a style repository"
    }

    @Override
    GetStyleRepositoryOptions getOptions() {
        new GetStyleRepositoryOptions()
    }

    @Override
    void execute(GetStyleRepositoryOptions options, Reader reader, Writer writer) throws Exception {
        StyleRepository styleRepository = StyleRepositoryFactory.getStyleRepository(options.type, options.params)
        String style = styleRepository.getForLayer(options.layerName, options.styleName)
        if (options.outputFile) {
            options.outputFile.text = style
        } else {
            writer.write(style)
        }
    }

    static class GetStyleRepositoryOptions extends Options {

        @Option(name = "-t", aliases = "--type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String type = "directory"

        @Option(name = "-o", aliases = "--options", usage = "The style repository options", required = false)
        Map params = [:]

        @Option(name = "-l", aliases = "--layer-name", usage = "The layer name", required = true)
        String layerName

        @Option(name = "-s", aliases = "--style-name", usage = "The style name", required = true)
        String styleName

        @Option(name = "-f", aliases = "--output-file", usage = "The output file", required = false)
        File outputFile

    }

}
