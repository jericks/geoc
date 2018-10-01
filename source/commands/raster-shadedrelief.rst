raster shadedrelief
===================

**Name**:

geoc raster shadedrelief

**Description**:

Create a shaded relief raster

**Arguments**:

   * -s --scale: The scale

   * -a --altitude: The altitude

   * -m --azimuth: The azimuth

   * -x --resx: The x resolution

   * -y --resy: The y resolution

   * -z --zeta-factory: The zeta factory

   * -g --algorithm: The algorithm

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster shadedrelief -i elev.tif -o shadedrelief -s 1.0 -a 45.0 -m 15.0