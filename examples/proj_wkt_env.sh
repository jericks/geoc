#!/bin/bash
echo "Projection WKT:"
geoc proj wkt -e EPSG:4326

echo "Projection Envelope:"
geoc proj envelope -e EPSG:2927

geoc proj envelope -e EPSG:2927 -g -o proj_epsg2927.shp

geoc vector defaultstyle --color "#0066FF" -o 0.15 -g polygon > proj_epsg2927.sld

geoc map draw -f proj_epsg2927.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=proj_epsg2927.shp" \
    -b "-163.037109,11.178402,-36.474609,60.716198,EPSG:4326"
