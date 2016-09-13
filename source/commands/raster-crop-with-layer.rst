raster crop with layer
======================

**Name**:

geoc raster crop with layer

**Description**:

Crop a Raster using the geometry from a Layer

**Arguments**:

   * -w --input-workspace: The input workspace

   * -y --input-layer: The input layer

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster crop with layer -i alki.tif -o alki_cropped.tif -w poly.shp