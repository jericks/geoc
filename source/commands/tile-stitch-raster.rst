tile stitch raster
==================

**Name**:

geoc tile stitch raster

**Description**:

Stitch image tiles together to create a Raster

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

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * --help : Print the help message



**Example**::

    geoc tile stitch raster -l earthquakes.mbtiles -z 1 -o earthquakes.tif