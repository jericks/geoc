#!/bin/bash
geoc tile vector grid -l "type=osm" -z 2 -o "type=shapefile file=grid.shp" -r grid

geoc vector defaultstyle --color wheat -o 0.15 -g polygon > grid.sld

geoc map draw -f tile_vector_grid.png \
    -l "layertype=tile type=osm" \
    -l "layertype=layer file=grid.shp layername=grid style=grid.sld"
