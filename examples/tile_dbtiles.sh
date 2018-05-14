#!/bin/bash
rm countries.db.*

geoc tile generate -l "type=dbtiles url='jdbc:h2:countries.db' driver='org.h2.Driver' name=Countries description='Natural Earth'" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4

geoc tile pyramid -l "type=dbtiles url='jdbc:h2:countries.db' driver='org.h2.Driver'"  -o text

geoc tile stitch raster -l "type=dbtiles url='jdbc:h2:countries.db' driver='org.h2.Driver'" -o countries_1.png -z 1
