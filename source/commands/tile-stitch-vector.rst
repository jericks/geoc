tile stitch vector
==================

**Name**:

geoc tile stitch vector

**Description**:

Stitch vector tiles together to create a one or more Layers

**Arguments**:

   * -l --tile-layer: The tile layer

   * -b --bounds: The bounds

   * -w --width: The raster width

   * -h --height: The raster height

   * -z --zoom-level: The tile zoom level

   * -x --minx: The min x or col

   * -y --miny: The min y or row

   * -c --maxx: The max x or col

   * -u --maxy: The max y or row

   * -o --output-workspace: The output workspace

   * --help : Print the help message



**Example**::

    geoc tile stitch vector -l "type=vectortiles format=mvt file=earthquakes/mvt name=earthquakes" -z 1 -o earthquakes.gpkg