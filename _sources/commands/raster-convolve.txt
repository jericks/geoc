raster convolve
===============

**Name**:

geoc raster convolve

**Description**:

Convolve the values of a Raster

**Arguments**:

   * -w --width: The kernel width

   * -h --height: The kernel height

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster convolve -i raster.tif -o raster_convolved.tif -w 2 -h 3