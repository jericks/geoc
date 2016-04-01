#!/bin/bash

echo "WKT to GeoJSON Geometry"
geoc geometry convert -i "POINT (1 2)" -f geojson

echo "WKT to GeoJSON Feature"
geoc geometry convert -i "POINT (1 2)" -f geojson -t feature

echo "WKT to GeoJSON Layer"
geoc geometry convert -i "POINT (1 2)" -f geojson -t layer

echo "WKT to WKB"
geoc geometry convert -i "POINT (1 2)" -f wkb

echo "WKT to GeoBuf"
geoc geometry convert -i "POINT (1 2)" -f geobuf

echo "WKT to KML Geometry"
geoc geometry convert -i "POINT (1 2)" -f kml -t geometry

echo "WKT to KML Feature"
geoc geometry convert -i "POINT (1 2)" -f kml -t feature

echo "WKT to KML Layer"
geoc geometry convert -i "POINT (1 2)" -f kml -t layer
