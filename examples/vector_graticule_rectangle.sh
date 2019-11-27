#!/bin/bash

geoc vector graticule rectangle -g -180,-90,180,90 -o rectangles.shp -w 10 -h 20

geoc map draw -f vector_graticule_rectangle.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=rectangles.shp style='stroke=#ab9b7d stroke-width=0.5 fill=#f5deb3 fill-opacity=0.15'"
