#!/bin/bash
mkdir tms

geoc tile generate -l "type=tms file=tms format=jpeg" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4 \
    -v

geoc tile pyramid -l "type=tms file=tms format=jpeg" -o xml

geoc tile stitch raster -l "type=tms file=tms format=jpeg" -o countries_1.png -z 1