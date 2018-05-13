#!/bin/bash
geoc vector randompoints -g -180,-90,180,90 -n 100 | geoc vector buffer -d 4 -o polys1.shp

geoc vector randompoints -g -180,-90,180,90 -n 10 | geoc vector buffer -d 8 -o polys2.shp

geoc vector intersects -i polys1.shp -k polys2.shp > intersectingPolys.csv

cat intersectingPolys.csv | geoc vector defaultstyle --color red -o 0.75 > intersectingPolys.sld

geoc map draw -f vector_intersects.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=polys1.shp" \
    -l "layertype=layer file=polys2.shp" \
    -l "layertype=layer file=intersectingPolys.csv style=intersectingPolys.sld"