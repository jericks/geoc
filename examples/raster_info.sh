#!/bin/bash
echo "Info"
geoc raster info -i earth.tif

echo "Size"
geoc raster size -i earth.tif

echo "Projection"
geoc raster projection -i earth.tif
