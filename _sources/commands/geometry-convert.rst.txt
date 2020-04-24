geometry convert
================

**Name**:

geoc geometry convert

**Description**:

Convert a geometry from one format to another

**Arguments**:

   * -i --input: The input geometry

   * -f --format: The output format (wkt, geojson, gml2, gml3, kml, georss, gpx, csv, wkb)

   * -p --format-options: The output format options

   * -t --type: The output type (geometry, feature, layer)

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc geometry convert -i "POINT (-122 48)" -f geojson