#!/bin/bash
rm countries.mbtiles

geoc tile generate -l countries.mbtiles \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4

geoc tile pyramid -l countries.mbtiles -o text

geoc tile stitch raster -l countries.mbtiles -o countries_1.png -z 1