#!/bin/bash
geoc raster draw -f earth_countries.png -i earth.tif -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
