raster extractfootprint
=======================

**Name**:

geoc raster extractfootprint

**Description**:

Extract the footprint of the Raster as a Vector Layer

**Arguments**:

   * -e --exclusion-range: A comma delimited range of values to exclude from the search.

   * -t --threshold-area: A number used to exclude small Polygons.  The default is 5.

   * -f --compute-simplified-footprint: Whether to compute a simplified footprint or not.  The default is false.

   * -s --simplifier-factor: A number used to simplify the geometry. The default is 2.

   * -c --remove-collinear: Whether to remove collinear coordinates. The default is true.

   * -v --force-valid: Whether to force creation of valid polygons.  The default is true.

   * -y --loading-type: The image loading type (Deferred or Immediate). Immediate is the default.

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster extractfootprint -i raster.tif -o footprint.shp