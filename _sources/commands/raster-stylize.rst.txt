raster stylize
==============

**Name**:

geoc raster stylize

**Description**:

Create a new Raster by baking the style into an existing Raster

**Arguments**:

   * -s --style: The SLD style file

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster stylize -i raster.tif -o raster_stylized.tif -s raster_colormap.sld