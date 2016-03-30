#!/bin/bash

echo "Attribute filter from option:"
geoc filter cql2xml -c "PERSONS > 15000000"

echo "Attribute filter from stdin:"
echo "PERSONS BETWEEN 1000000 AND 3000000" | geoc filter cql2xml

echo "Spatial filter from option:"
geoc filter cql2xml -c "BBOX(the_geom, -90, 40, -60, 45)"

echo "Spatial filter from stdin:"
echo "DISJOINT(the_geom, POLYGON((-90 40, -90 45, -60 45, -60 40, -90 40)))" | geoc filter cql2xml