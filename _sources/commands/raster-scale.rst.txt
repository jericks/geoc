raster scale
============

**Name**:

geoc raster scale

**Description**:

Scale a Raster

**Arguments**:

   * -x --x-scale: The scale factor along the x axis

   * -y --y-scale: The scale factor along the y axis

   * -t --x-translate: The x translation

   * -r --y-translate: The y translation

   * -n --interpolation: The interpolation method (bicubic, bicubic2, bilinear, nearest)

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster scale -i raster.tif -x 2 -y 3 -o raster_scaled.tif