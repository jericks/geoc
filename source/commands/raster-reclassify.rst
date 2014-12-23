raster reclassify
=================

**Name**:

geoc raster reclassify

**Description**:

Reclassify a Raster

**Arguments**:

   * -b --band: The band

   * -n --nodata: The NODATA value

   * -r --range: A range: from-to=value or 1-10=5

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message



**Example**::

    geoc raster reclassify -i raster.tif -o raster_reclass.tif -r 49-100=1 -r 100-256=255