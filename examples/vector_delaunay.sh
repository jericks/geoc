#!/bin/bash
geoc vector centroid -i naturalearth.gpkg -l countries | geoc vector delaunay -o countries_delaunay.pbf

geoc vector defaultstyle --color navy -o 0.15 -g polygon > countries_delaunay.sld

geoc map draw -f vector_delaunay.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=countries_delaunay.pbf layername=countries_delaunay style=countries_delaunay.sld"

