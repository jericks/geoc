raster get value
================

**Name**:

geoc raster get value

**Description**:

Get the cell value from a Raster

**Arguments**:

   * -x --x-coordinate: The x coordinate

   * -y --y-coordinate: The y coordinate

   * -t --type: The type can be point or pixel

   * -b --band: The band to get a value from

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message



**Example**::

    geoc raster get value -i alki.tif -x 5 -y 5 -t pixel