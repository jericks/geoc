#!/bin/bash
geoc vector uniquevaluesstyle -i naturalearth.gpkg -l countries -f MAP_COLOR -c GreenToRedOrange > countries_map_color.sld

geoc map draw -f vector_uniquevalues.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries_map_color.sld"