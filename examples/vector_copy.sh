#!/bin/bash
geoc vector copy -i naturalearth.gpkg -l countries -o naturalearth.sqlite -r countries

geoc vector copy -i naturalearth.gpkg -l ocean -o naturalearth.sqlite  -r ocean

geoc vector copy -i naturalearth.gpkg -l places -o naturalearth.sqlite  -r places

geoc vector copy -i naturalearth.gpkg -l states -o naturalearth.sqlite  -r states

geoc vector defaultstyle --color '#E3E3E3' -g polygon > states.sld

geoc vector defaultstyle --color '#24C7FF' -g places > places.sld

geoc map draw -f vector_copy.png \
    -l "layertype=layer file=naturalearth.sqlite layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.sqlite layername=countries style=countries.sld" \
    -l "layertype=layer file=naturalearth.sqlite layername=states style=states.sld" \
    -l "layertype=layer file=naturalearth.sqlite layername=places style=places.sld"

