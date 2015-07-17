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

        TileLayer tileLayer
        TileRenderer tileRenderer

        List layers = []
        org.geocommands.Util.addBasemap(options.baseMap, layers)

        if (options.tileLayer.endsWith(".mbtiles")) {
            tileLayer = new MBTiles(new File(options.tileLayer), options.tileLayerName, options.tileLayerName)
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (options.tileLayer.endsWith(".gpkg")) {
            Pyramid pyramid = PyramidUtil.readPyramid(options.pyramid)
            tileLayer = new GeoPackage(new File(options.tileLayer), options.tileLayerName, pyramid)
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (options.type in ["png","jpeg","jpg","gif"] && new File(options.tileLayer).isDirectory()) {
            Pyramid pyramid = PyramidUtil.readPyramid(options.pyramid)
            tileLayer = new TMS(options.tileLayerName, options.type, new File(options.tileLayer), pyramid)
            tileRenderer = new ImageTileRenderer(tileLayer, layers)
        } else if (options.type.equalsIgnoreCase("utfgrid") && new File(options.tileLayer).isDirectory()) {
            tileLayer = new UTFGrid(new File(options.tileLayer))
            Layer layer = layers[0]
            List fields = options.fields.collect { layer.schema.get(it) }
            tileRenderer = new UTFGridTileRenderer(tileLayer, layers[0], fields)
        } else if (options.type.toLowerCase() in ["mvt","json", "geojson", "csv", "georss", "gml", "gpx", "kml"] && new File(options.tileLayer).isDirectory()) {
            geoscript.layer.io.Writer layerWriter
            if (options.type.equalsIgnoreCase("mvt")) {
                layerWriter = new MvtWriter()
            } else if (options.type.toLowerCase() in ["json", "geojson"]) {
                layerWriter = new GeoJSONWriter()
            } else if (options.type.equalsIgnoreCase("csv")) {
                layerWriter = new CsvWriter()
            } else if (options.type.equalsIgnoreCase("georss")) {
                layerWriter = new GeoRSSWriter()
            } else if (options.type.equalsIgnoreCase("gml")) {
                layerWriter = new GmlWriter()
            } else if (options.type.equalsIgnoreCase("gpx")) {
                layerWriter = new GpxWriter()
            } else if (options.type.equalsIgnoreCase("kml")) {
                layerWriter = new KmlWriter()
            }
            Pyramid pyramid = PyramidUtil.readPyramid(options.pyramid)
            tileLayer = new VectorTiles(options.tileLayerName, new File(options.tileLayer), pyramid, options.type)
            tileRenderer = new VectorTileRenderer(layerWriter, layers[0], options.fields)
        } else if (options.type.equalsIgnoreCase("pbf") && new File(options.tileLayer).isDirectory()) {
            Pyramid pyramid = PyramidUtil.readPyramid(options.pyramid)
            tileLayer = new VectorTiles(options.tileLayerName, new File(options.tileLayer), pyramid, options.type)
            Map<String,List> layerFields = options.fieldMap.isEmpty() ? null : [:]
            options.fieldMap.each{String key, String value ->
                layerFields[key] = value.split(",") as List
            }
            tileRenderer = new PbfVectorTileRenderer(layers, layerFields)
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

        @Option(name = "-n", aliases = "--tile-layer-name", usage = "The tile layer name", required = true)
        String tileLayerName

        @Option(name = "-t", aliases = "--type", usage = "The type of tile layer(png, utfgrid, mvt, pbf)", required = false)
        String type = "png"

        @Option(name = "-p", aliases = "--pyramid", usage = "The pyramid", required = false)
        String pyramid = "GlobalMercator"

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
