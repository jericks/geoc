raster polygon
==============

**Name**:

geoc raster polygon

**Description**:

Convert a Raster to a Polygon Vector Layer

**Arguments**:

   * -b --band: The band

   * -e --inside-edges: Whether to include inside edges

   * -g --region-of-interest: The region of interest

   * -n --no-data: A no data value

   * -a --range: A range (min,minIncluded,max,maxIncluded)

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message



**Example**::

    geoc raster polygon -i raster.tif -o polygons.shp