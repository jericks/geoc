#!/bin/bash
geoc tile generate -l "type=geopackage file=tiles.gpkg name=world pyramid=geodetic" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4 \
    -v

geoc tile pyramid -l "type=geopackage file=tiles.gpkg name=world" -o json

geoc tile stitch raster -l "type=geopackage file=tiles.gpkg name=world" -o countries_2.png -z 2