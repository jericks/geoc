#!/bin/bash
geoc vector gradientstyle -i naturalearth.gpkg -l countries -f PEOPLE -n 6 -c greens > countries_gradient_green.sld

geoc map draw -f vector_gradient_greens.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries_gradient_green.sld"

geoc vector gradientstyle -i naturalearth.gpkg -l countries -f PEOPLE -n 8 -c reds > countries_gradient_red.sld

geoc map draw -f vector_gradient_reds.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries_gradient_red.sld"