#!/bin/sh

# Download SRTM data for washington state
curl -O http://srtm.csi.cgiar.org/SRT-ZIP/SRTM_V41/SRTM_Data_GeoTiff/srtm_12_03.zip

# Unzip data
unzip srtm_12_03.zip

# Crop raster to Pierce County
geoc raster crop -i srtm_12_03.tif -b -123.552246,46.253948,-120.739746,47.522765 -o pc.tif

# Create Shaded Relief
geoc raster shadedrelief -i pc.tif -o pc_shadedrelief.tif -s 1.0 -a 45 -m 15

# Create a map
geoc map draw -f pc_shadedrelief.png -l "layertype=raster source=pc_shadedrelief.tif"


