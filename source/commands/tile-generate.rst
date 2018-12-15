tile generate
=============

**Name**:

geoc tile generate

**Description**:

Generate tiles

**Arguments**:

   * -l --tile-layer: The tile layer

   * -f --field: A field

   * -d --layer-fields: A List of sub fields for a layer

   * -m --layer: The map layer

   * -s --start-zoom: The start zoom level

   * -e --end-zoom: The end zoom level

   * -b --bounds: The bounds

   * -t --metatile: The metatile width,height

   * -i --missing: Whether to generate only missing tiles

   * -v --verbose: The verbose flag

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc tile generate -l earthquakes.mbtiles -m layerFile -s 0 -e 2 -v false