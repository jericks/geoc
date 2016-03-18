#!/bin/bash
mkdir mvt

geoc tile generate -l "type=vectortiles file=mvt format=mvt" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 4 \
    -v

geoc tile pyramid -l "type=vectortiles file=mvt format=mvt" -o xml

geoc tile stitch vector -l "type=vectortiles file=mvt format=mvt" -o "url=mvt" -z 1