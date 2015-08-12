package org.geocommands.tile

import geoscript.geom.Bounds
import geoscript.layer.*
import geoscript.layer.io.CsvWriter
import geoscript.layer.io.GeoJSONWriter
import geoscript.layer.io.GeoRSSWriter
import geoscript.layer.io.GmlWriter
import geoscript.layer.io.GpxWriter
import geoscript.layer.io.KmlWriter
import geoscript.layer.io.MvtWriter
import org.geocommands.Command
import org.geocommands.Options
import org.kohsuke.args4j.Option

import java.io.Writer

/**
 * Generate tiles
 * @author Jared Erickson
 */
class GenerateCommand extends Command<GenerateOptions> {

    @Override
    String getName() {
        "tile generate"
    }

    @Override
    String getDescription() {
        "Generate tiles"
    }

    @Override
    GenerateOptions getOptions() {
        new GenerateOptions()
    }

    @Override
    void execute(GenerateOptions options, Reader reader, Writer writer) throws Exception {

        TileLayer tileLayer = TileLayer.getTileLayer(options.tileLayer)
        TileRenderer tileRenderer

        List layers = []
        org.geocommands.Util.addBasemap(options.baseMap, layers)

        if (tileLayer instanceof MBTiles) {
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (tileLayer instanceof GeoPackage) {
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (tileLayer instanceof TMS) {
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (tileLayer instanceof UTFGrid) {
            Layer layer = layers[0]
            List fields = options.fields.collect { layer.schema.get(it) }
            tileRenderer = new UTFGridTileRenderer(tileLayer, layers[0], fields)
        } else if (tileLayer instanceof VectorTiles) {
            VectorTiles vectorTiles = tileLayer as VectorTiles
            if (vectorTiles.type.equalsIgnoreCase("pbf")) {
                Map<String,List> layerFields = options.fieldMap.isEmpty() ? null : [:]
                options.fieldMap.each{String key, String value ->
                    layerFields[key] = value.split(",") as List
                }
                tileRenderer = new PbfVectorTileRenderer(layers, layerFields)
            } else {
                geoscript.layer.io.Writer layerWriter
                if (vectorTiles.type.equalsIgnoreCase("mvt")) {
                    layerWriter = new MvtWriter()
                } else if (vectorTiles.type.toLowerCase() in ["json", "geojson"]) {
                    layerWriter = new GeoJSONWriter()
                } else if (vectorTiles.type.equalsIgnoreCase("csv")) {
                    layerWriter = new CsvWriter()
                } else if (vectorTiles.type.equalsIgnoreCase("georss")) {
                    layerWriter = new GeoRSSWriter()
                } else if (vectorTiles.type.equalsIgnoreCase("gml")) {
                    layerWriter = new GmlWriter()
                } else if (vectorTiles.type.equalsIgnoreCase("gpx")) {
                    layerWriter = new GpxWriter()
                } else if (vectorTiles.type.equalsIgnoreCase("kml")) {
                    layerWriter = new KmlWriter()
                }
                tileRenderer = new VectorTileRenderer(layerWriter, layers[0], options.fields)
            }
        }

        TileGenerator generator = new TileGenerator(verbose: options.verbose)
        try {
            generator.generate(tileLayer, tileRenderer, options.startZoom, options.endZoom,
                    bounds: Bounds.fromString(options.bounds), missingOnly: options.missing)
        } finally {
            tileLayer.close()
        }
    }

    static class GenerateOptions extends Options {

        @Option(name = "-l", aliases = "--tile-layer", usage = "The tile layer", required = true)
        String tileLayer

        @Option(name = "-f", aliases = "--field", usage = "A field", required = false)
        List<String> fields = []

        @Option(name = "-d", aliases = "--layer-fields", usage = "A List of sub fields for a layer", required = false)
        Map<String,String> fieldMap = [:]

        @Option(name = "-m", aliases = "--base-map", usage = "The base map", required = true)
        String baseMap

        @Option(name = "-s", aliases = "--start-zoom", usage = "The start zoom level", required = true)
        int startZoom

        @Option(name = "-e", aliases = "--end-zoom", usage = "The end zoom level", required = true)
        int endZoom

        @Option(name = "-b", aliases = "--bounds", usage = "The bounds", required = false)
        String bounds

        @Option(name = "-i", aliases = "--missing", usage = "Whether to generate only missing tiles", required = false)
        boolean missing = false

        @Option(name = "-v", aliases = "--verbose", usage = "The verbose flag", required = false)
        boolean verbose = false
    }

}
