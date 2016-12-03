#!/bin/bash

echo "Convert point buffered by geom to a CSV Layer with a single Feature"
echo "POINT (-122.386 47.583)" | geom buffer -d 0.2 | geoc vector geomr
echo ""

echo "Convert points from polygon created by geom into a CSV Layer with multiple Features"
echo "POINT (-122.386 47.583)" | geom buffer -d 0.2 | geom coordinates | geom dump | geoc vector geomr
echo ""
