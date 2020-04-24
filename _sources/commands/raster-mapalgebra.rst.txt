raster mapalgebra
=================

**Name**:

geoc raster mapalgebra

**Description**:

Perform map algebra

**Arguments**:

   * -s --script: The map algebra (jiffle) script

   * -r --raster: An input Raster

   * -b --bounds: The bounds

   * -z --size: The size

   * -n --output-name: The output name

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -p --output-raster-projection: The output raster projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster mapalgebra -s "dest = r1 * r2" -r "r1=raster1.acs" -r "r2=raster2.acs" -o raster_add.tif -p "EPSG:4326"