package org.geocommands.style


import geoscript.style.StyleRepository
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

class CopyStyleRepositoryCommand extends Command<CopyStyleRepositoryOptions> {

    @Override
    String getName() {
        "style repository copy"
    }

    @Override
    String getDescription() {
        "Copy styles from one repository to another"
    }

    @Override
    CopyStyleRepositoryOptions getOptions() {
        new CopyStyleRepositoryOptions()
    }

    @Override
    void execute(CopyStyleRepositoryOptions options, Reader reader, Writer writer) throws Exception {
        StyleRepository inputStyleRepository = StyleRepositoryFactory.getStyleRepository(options.inputType, options.inputParams)
        StyleRepository outputStyleRepository = StyleRepositoryFactory.getStyleRepository(options.outputType, options.outputParams)
        List<Map<String, String>> styles = inputStyleRepository.getAll()
        styles.each {Map<String,String> style ->
            outputStyleRepository.save(style.layerName, style.styleName, style.style)
        }
    }

    static class CopyStyleRepositoryOptions extends Options {

        @Option(name = "-t", aliases = "--input-type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String inputType = "directory"

        @Option(name = "-p", aliases = "--input-options", usage = "The style repository options", required = false)
        Map inputParams = [:]

        @Option(name = "-o", aliases = "--output-type", usage = "The type of style repository (directory, nested-directory, h2, sqlite, postgres)", required = false)
        String outputType = "directory"

        @Option(name = "-r", aliases = "--output-options", usage = "The style repository options", required = false)
        Map outputParams = [:]
        
    }

}
