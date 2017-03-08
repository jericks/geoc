#!/bin/sh

# Download SRTM data for washington state
curl -O http://srtm.csi.cgiar.org/SRT-ZIP/SRTM_V41/SRTM_Data_GeoTiff/srtm_12_03.zip

# Unzip data
unzip srtm_12_03.zip

# View info
geoc raster info -i srtm_12_03.tif

# Crop raster to Pierce County
geoc raster crop -i srtm_12_03.tif -b -123.552246,46.253948,-120.739746,47.522765 -o pc.tif

# Create contours
geoc raster contour -i pc.tif -o contours.shp -v 300 -s -m

# Create a map
geoc style create -s stroke=black -s stroke-width=0.1 -t sld > contours.sld

geoc raster style colormap -v 25=#a6611a -v 473.2=#dfc27d -v 921.5=#f5f5f5 -v 1370=#80cdc1 -v 1818=#018571 > pc.sld

geoc map draw -f pc.png -l "layertype=raster source=pc.tif style=pc.sld" -l "layertype=layer file=contours.shp style=contours.sld"


