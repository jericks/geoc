#!/bin/bash

echo "Decimal Degrees to XY:"
geoc geometry dd2pt -d "122d 19m 59.0016s W, 47d 36m 34.9992s N" -t xy

echo "Decimal Degrees to WKT:"
geoc geometry dd2pt -d "122d 19m 59.0016s W, 47d 36m 34.9992s N" -t wkt

echo "Decimal Degrees to GeoJSON:"
geoc geometry dd2pt -d "122d 19m 59.0016s W, 47d 36m 34.9992s N" -t json

echo "Point to DMS:"
geoc geometry pt2dd -p "POINT (-122.5256194 47.212022222)" -t dms

echo "Point to DMS with characters:"
geoc geometry pt2dd -p "POINT (-122.5256194 47.212022222)" -t dms_char

echo "Point to DDM:"
geoc geometry pt2dd -p "POINT (-122.5256194 47.212022222)" -t ddm

echo "Point to DDM with characters:"
geoc geometry pt2dd -p "POINT (-122.5256194 47.212022222)" -t ddm_char