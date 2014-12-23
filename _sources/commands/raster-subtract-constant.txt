raster subtract constant
========================

**Name**:

geoc raster subtract constant

**Description**:

Substract a constant value to a Raster

**Arguments**:

   * -v --value: The value

   * -m --from: Whether to subtract the Raster from the constant or vice verse

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message



**Example**::

    geoc raster subtract constant -i raster.tif -v 50 -o raster_minus_50.tif