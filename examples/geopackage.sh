#!/bin/bash
rm data.gpkg

# Geodetic Tiles
geoc tile generate -l "type=geopackage file=data.gpkg name=world pyramid=geodetic" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 3 \
    -v

# Mercator Tiles
geoc tile generate -l "type=geopackage file=data.gpkg name=world_mercator pyramid=mercator" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -s 0 \
    -e 3 \
    -v

# Add Ocean Vector Layer
geoc vector copy -i naturalearth.gpkg -l ocean -o data.gpkg -r ocean

# Add Countries Vector Layer
geoc vector copy -i naturalearth.gpkg -l countries -o data.gpkg -r countries

# Add States Vector Layer
geoc vector copy -i naturalearth.gpkg -l states -o data.gpkg -r states

# Add Places Vector Layer
geoc vector copy -i naturalearth.gpkg -l places -o data.gpkg -r places