#!/bin/bash
geoc raster display -i earth.tif -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
