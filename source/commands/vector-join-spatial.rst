vector join spatial
===================

**Name**:

geoc vector join spatial

**Description**:

Spatially join two layers to create the output Layer.

**Arguments**:

   * -f --field: A Field name

   * -t --spatial-type: The spatial type (intersects, contains). Defaults to intersects.

   * -m --multiple-type: The multiple type (first, closest, largest). Defaults to first.

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector join spatial -i points.shp -k polygons.shp -o points_joined.shp -f name -f description -f homepage