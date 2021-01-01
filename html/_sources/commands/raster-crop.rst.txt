raster crop
===========

**Name**:

geoc raster crop

**Description**:

Crop a Raster

**Arguments**:

   * -b --bound: The Bounds

   * -x --pixel: Whether the Bounds is pixel or geographic

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster crop -i raster.tif -b "-120,-40,120,40" -o raster_croped.tif