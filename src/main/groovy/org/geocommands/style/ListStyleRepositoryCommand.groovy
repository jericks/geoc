package org.geocommands.style


import geoscript.style.StyleRepository
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class ListStyleRepositoryCommand extends Command<ListStyleRepositoryOptions> {

    @Override
    String getName() {
        "style repository list"
    }

    @Override
    String getDescription() {
        "List styles from a style repository"
    }

    @Override
    ListStyleRepositoryOptions getOptions() {
        new ListStyleRepositoryOptions()
    }

    @Override
    void execute(ListStyleRepositoryOptions options, Reader reader, Writer writer) throws Exception {
        StyleRepository styleRepository = StyleRepositoryFactory.getStyleRepository(options.type, options.params)
        List<Map<String,String>> styles = []
        if (options.layerName) {
            styles.addAll(styleRepository.getForLayer(options.layerName))
        } else {
            styles.addAll(styleRepository.getAll())
        }
        String NEW_LINE = System.getProperty("line.separator")
        styles.each { Map<String,String> style ->
            writer.write(style.layerName + " " + style.styleName)
            writer.write(NEW_LINE)
        }
    }

    static class ListStyleRepositoryOptions extends Options {

        @Option(name = "-t", aliases = "--type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String type = "directory"

        @Option(name = "-o", aliases = "--options", usage = "The style repository options", required = false)
        Map params = [:]

        @Option(name = "-l", aliases = "--layer-name", usage = "The layer name", required = false)
        String layerName

    }

}
