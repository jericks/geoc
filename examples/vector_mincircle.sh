#!/bin/bash
rm mincircle.sqlite

geoc vector mincircle -i naturalearth.gpkg -l places -o mincircle.sqlite -r envelope
geoc vector mincircles -i naturalearth.gpkg -l countries -o mincircle.sqlite -r envelopes

geoc vector defaultstyle --color "#0066FF" -o 0.15 -g linestring > mincircle.sld
geoc vector defaultstyle --color navy -o 0.15 -g polygon > mincircles.sld

geoc map draw -f vector_mincircle.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer dbtype=spatialite database=mincircle.sqlite layername=envelope style=mincircle.sld" \
    -l "layertype=layer dbtype=spatialite database=mincircle.sqlite layername=envelopes style=mincircles.sld"


