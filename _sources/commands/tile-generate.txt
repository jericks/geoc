tile generate
=============

**Name**:

geoc tile generate

**Description**:

Generate tiles

**Arguments**:

   * -l --tile-layer: The tile layer

   * -n --tile-layer-name: The tile layer name

   * -t --type: The type of tile layer(png, utfgrid, mvt, pbf)

   * -p --pyramid: The pyramid

   * -f --field: A field

   * -d --layer-fields: A List of sub fields for a layer

   * -m --base-map: The base map

   * -s --start-zoom: The start zoom level

   * -e --end-zoom: The end zoom level

   * -b --bounds: The bounds

   * -v --verbose: The verbose flag

   * --help : Print the help message



**Example**::

    geoc tile generate -l earthquakes.mbtiles -n earthquakes -m layerFile -s 0 -e 2 -v false