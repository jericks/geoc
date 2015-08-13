tile vector grid
================

**Name**:

geoc tile vector grid

**Description**:

Create a vector grid of a tile layers cells.

**Arguments**:

   * -l --tile-layer: The tile layer

   * -b --bounds: The bounds

   * -z --zoom-level: The tile zoom level

   * -x --minx: The min x or col

   * -y --miny: The min y or row

   * -c --maxx: The max x or col

   * -u --maxy: The max y or row

   * -w --width: The raster width

   * -h --height: The raster height

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message



**Example**::

    geoc tile vector grid -l earthquakes.mbtiles -z 1 -o grid.shp