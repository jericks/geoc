raster multiply
===============

**Name**:

geoc raster multiply

**Description**:

Multiply two Rasters together

**Arguments**:

   * -k --other-raster: The other raster

   * -y --other-raster-name: The other raster name

   * -j --other-projection: The other projection

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster multiply -i raster1.acs -p "EPSG:4326" -k raster2.acs -j "EPSG:4326" -o raster_multiplied.tif