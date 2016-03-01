#!/bin/bash
geoc vector randompoints -g -180,-90,180,90 -n 100 > points.csv

cat points.csv | geoc vector buffer -d 10 > polygons.csv

cat points.csv | geoc vector defaultstyle --color navy -o 0.75 > points.sld

cat polygons.csv | geoc vector defaultstyle --color silver -o 0.5 > polygons.sld

geoc map draw -f vector_buffer.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=polygons.csv style=polygons.sld" \
    -l "layertype=layer file=points.csv style=points.sld"