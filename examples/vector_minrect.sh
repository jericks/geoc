#!/bin/bash
geoc vector minrect -i naturalearth.gpkg -l places -o "DatasourceName=minrect.shp DriverName='ESRI Shapefile'" -r minrect
geoc vector minrects -i naturalearth.gpkg -l countries -o "DatasourceName=minrects.shp DriverName='ESRI Shapefile'" -r minrects

geoc vector defaultstyle --color "#0066FF" -o 0.15 -g linestring > minrect.sld
geoc vector defaultstyle --color navy -o 0.15 -g polygon > minrects.sld

geoc map draw -f vector_minrect.png -b "-180,-90,180,90,EPSG:4326" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer DatasourceName=minrect.shp DriverName='ESRI Shapefile' layername=minrect style=minrect.sld" \
    -l "layertype=layer DatasourceName=minrects.shp DriverName='ESRI Shapefile' layername=minrects style=minrects.sld"
