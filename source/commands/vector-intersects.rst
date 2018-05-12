vector intersects
=================

**Name**:

geoc vector intersects

**Description**:

Only include Features from the Input Layer that Intersect with Features from the Other Layer in the Output Layer.

**Arguments**:

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector intersects -i points.shp -k polygons.shp -o pointsIntersectingPolygons.shp