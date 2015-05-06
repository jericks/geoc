tile stitch vector
==================

**Name**:

geoc tile stitch vector

**Description**:

Stitch vector tiles together to create a one or more Layers

**Arguments**:

   * -l --tile-layer: The tile layer

   * -n --tile-layer-name: The tile layer name

   * -t --type: The type of tile layer(png, utfgrid, mvt, pbf)

   * -p --pyramid: The pyramid

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

    geoc tile stitch vector -l earthquakes/mvt -z 1 -o earthquakes.gpkg