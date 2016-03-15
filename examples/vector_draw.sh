#!/bin/bash
geoc vector draw -f vector_draw.png -i naturalearth.gpkg -l countries -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
