#!/bin/bash
rm -r json
mkdir json

geoc tile generate -l "type=vectortiles file=json format=json pyramid=GlobalMercatorTopLeft" \
    -m "layertype=layer file=naturalearth.gpkg layername=ocean" \
    -m "layertype=layer file=naturalearth.gpkg layername=countries" \
    -s 0 \
    -e 4 \
    -v

cat <<EOT >> json.html
<!doctype html>
<html lang="en">
  <head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v4.6.5/css/ol.css" type="text/css">
    <style>
      .map {
        height: 400px;
        width: 100%;
      }
    </style>
    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v4.6.5/build/ol.js" type="text/javascript"></script>
    <title>GeoScript Vector Tiles</title>
  </head>
  <body>
    <h2>GeoScript Vector Tiles</h2>
    <div id="map" class="map"></div>
    <script type="text/javascript">
        var vectorLayer = new ol.layer.VectorTile({
          source: new ol.source.VectorTile({
            format: new ol.format.GeoJSON(),
            projection: 'EPSG:3857',
            tileGrid: new ol.tilegrid.createXYZ({minZoom: 0, maxZoom: 6}),
            url: 'http://localhost:8000/json/{z}/{x}/{y}.json'
          }),
          style: new ol.style.Style({
            stroke: new ol.style.Stroke({
               color: 'black'
            }),
            fill: new ol.style.Fill({
              color: [238,238,238,0.1],
              opacity: 0.1
            })
          })
        });

        var map = new ol.Map({
            target: 'map',
            layers: [
              new ol.layer.Tile({
                source: new ol.source.OSM()
              }), vectorLayer
            ],
            view: new ol.View({
              center: ol.proj.transform([-100, 40], 'EPSG:4326', 'EPSG:3857'),
              zoom: 4
            })
        });
    </script>
  </body>
</html>
EOT

python -m SimpleHTTPServer
