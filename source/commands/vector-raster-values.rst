vector raster values
====================

**Name**:

geoc vector raster values

**Description**:

Get value from a Raster for each Feature's geometry

**Arguments**:

   * -n --field-name: The new value field name (defaults to value)

   * -t --field-type: The new value field type (defaults to double)

   * -b --band: The band to get values from (defaults to 0)

   * -s --input-raster: The input raster

   * -e --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector raster values -i points.shp -s raster.tif -o points_values.shp