vector distancewithin
=====================

**Name**:

geoc vector distancewithin

**Description**:

Only include Features from the Input Layer that are within a given distance of Features from the Other Layer in the Output Layer.

**Arguments**:

   * -d --distance: The distance

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector distancewithin -i points.shp -k polygons.shp -o pointsInPolygons.shp -d 4.56