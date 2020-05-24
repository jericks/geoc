#!/bin/bash
rm -r utf
mkdir utf

geoc tile generate -l "type=utfgrid file=utf" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -f "NAME" \
    -s 0 \
    -e 4 \
    -v

rm -r tms
mkdir tms

geoc tile generate -l "type=tms file=tms format=jpeg" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4 \
    -v \
    -t "4,4"
