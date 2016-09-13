raster crop with geometry
=========================

**Name**:

geoc raster crop with geometry

**Description**:

Crop a Raster with a Geometry

**Arguments**:

   * -g --geometry: The Geometry

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster crop with geometry -i alki.gif -o alki_cropped.tif -g "`geom buffer -g "POINT (1166476.232632274 823276.6023305996)" -d 50`"