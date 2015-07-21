tile delete
===========

**Name**:

geoc tile delete

**Description**:

Delete tiles from a tile layer

**Arguments**:

   * -l --tile-layer: The tile layer

   * -n --tile-layer-name: The tile layer name

   * -t --type: The type of tile layer(png, utfgrid, mvt, pbf)

   * -p --pyramid: The pyramid

   * -i --tile: The Tile Z/X/Y coordinates

   * -b --bounds: The bounds

   * -z --zoom-level: The tile zoom level

   * -x --minx: The min x or col

   * -y --miny: The min y or row

   * -c --maxx: The max x or col

   * -u --maxy: The max y or row

   * --help : Print the help message



**Example**::

    geoc tile delete -l earthquakes.mbtiles -n earthquakes -z 2