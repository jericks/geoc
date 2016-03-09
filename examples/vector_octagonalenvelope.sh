#!/bin/bash
geoc vector centroid -i naturalearth.gpkg -l countries | geoc vector octagonalenvelope -o countries_octagonalenvelope.shp
geoc vector octagonalenvelopes -i naturalearth.gpkg -l countries -o countries_octagonalenvelopes.shp

geoc vector defaultstyle --color "#0066FF" -o 0.15 -g line > countries_octagonalenvelope.sld
geoc vector defaultstyle --color navy -o 0.15 -g polygon > countries_octagonalenvelopes.sld

geoc map draw -f vector_octagonalenvelope.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=countries_octagonalenvelope.shp" \
    -l "layertype=layer file=countries_octagonalenvelopes.shp"

