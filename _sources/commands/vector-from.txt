vector from
===========

**Name**:

geoc vector from

**Description**:

Create a Layer from a string of KML, CSV, GML, GEORSS, GEOBUF, GPX or GeoJSON

**Arguments**:

   * -t --text: The text

   * -f --format: The string format (CSV, GeoJSON, KML, GML)

   * -g --geometry-type: The geometry type

   * -p --format-options: A format options 'key=value'

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message



**Example**::

    curl -s http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson | geoc vector from -f geojson