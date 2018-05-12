#!/bin/bash
geoc vector randompoints -g -180,-90,180,90 -n 1000 -o points.shp

geoc vector randompoints -g -180,-90,180,90 -n 10 | geoc vector buffer -d 8 -o polys.shp

geoc vector intersects -i points.shp -k polys.shp > pointsInPolys.csv

cat pointsInPolys.csv | geoc vector defaultstyle --color red -o 0.75 > pointsInPolys.sld

geoc map draw -f vector_intersects.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=polys.shp" \
    -l "layertype=layer file=points.shp" \
    -l "layertype=layer file=pointsInPolys.csv style=pointsInPolys.sld"